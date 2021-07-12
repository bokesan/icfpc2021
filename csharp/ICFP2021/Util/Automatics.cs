using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Threading;
using ICFP2021.Extensions;
using ICFP2021.ui.shapes;

namespace ICFP2021.Util
{
    public class Automatics
    {
        private bool _isFirstRun = true;
        
        private Random rnd = new Random();
        private readonly IEnumerable<WrappedPoint> _points;
        private readonly WrappedPolygon _polygon;
        public Automatics(IEnumerable<WrappedPoint> points, WrappedPolygon polygon)
        {
            this._points = points;
            this._polygon = polygon;
        }


        public void Run(Action refreshAction)
        {
            HashSet<WrappedPoint> frozen = new HashSet<WrappedPoint>();
            foreach (var snappedPoint in _points)
            {
                if (_isFirstRun == false && snappedPoint.Edges.All(x => x.State == EpsilonHelper.LineState.Okay))
                {
                    frozen.Add(snappedPoint);
                    foreach (var attached in snappedPoint.AttachedTo.Where(x => x.Edges.All(y => y.State == EpsilonHelper.LineState.Okay)))
                    {
                        frozen.Add(attached);
                    }
                    continue;
                }
                if (frozen.Contains(snappedPoint))
                    continue;

                double incrementX = rnd.Next(-10, 10);
                double incrementY = rnd.Next(-10, 10);
                if (incrementX % 3 == 0)
                {
                    incrementX = incrementX * -1;
                }
                if (incrementY % 4 == 0)
                {
                    incrementY = incrementY * -1;
                }

                if (_polygon.IsPointInPolygon(new Point(snappedPoint.X + incrementX, snappedPoint.Y - incrementY)))
                {
                    snappedPoint.Center = new Point(snappedPoint.X + incrementX, snappedPoint.Y - incrementY);
                    snappedPoint.X = snappedPoint.Center.X;
                    snappedPoint.Y = snappedPoint.Center.Y;
                }
            }

            _isFirstRun = false;
            refreshAction?.Invoke();
        }

        public void Randomize(Problem currentProblem, Action updateAction, Action exportAction)
        {
            bool areAllInBoundary = false;
            bool areAllDistancesChecked = false;
            var minX = _polygon.Points.Min(o => o.X);
            var maxX = _polygon.Points.Max(o => o.X);
            var minY = _polygon.Points.Min(o => o.Y);
            var maxY = _polygon.Points.Max(o => o.Y);
            while (!areAllInBoundary || !areAllDistancesChecked)
            {
                foreach (var a in _points)
                {
                    var x = rnd.Next((int)Math.Floor(minX), (int)Math.Floor(maxX));
                    var y = rnd.Next((int)Math.Floor(minY), (int)Math.Floor(maxY));
                    //Point p = new Point(x, y);
                    Application.Current.Dispatcher.Invoke(() =>
                    {
                        bool hadSuccess = false;
                        foreach (var edge in a.Edges)
                        {
                            var pointy = edge.GetPoints(100);
                            Point? closestSuccessfulPoint = null;
                            if (edge.State == EpsilonHelper.LineState.TooLong)
                            {
                                for (int i = pointy.Length - 1; i > 0; i--)
                                {
                                    if (_polygon.IsPointInPolygon(pointy[i]))
                                    {
                                        (EpsilonHelper.LineState, double) vals =
                                            EpsilonHelper.GetEpsilonRange(currentProblem, edge, pointy[i].X, edge.X2, pointy[i].Y, edge.Y2);
                                        if (vals.Item1 == EpsilonHelper.LineState.Okay)
                                        {
                                            closestSuccessfulPoint = pointy[i];
                                        }
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                            }
                            else if (edge.State == EpsilonHelper.LineState.TooShort)
                            {
                                for (int i = 0; i < pointy.Length; i++)
                                {
                                    if (_polygon.IsPointInPolygon(pointy[i]))
                                    {
                                        (EpsilonHelper.LineState, double) vals =
                                            EpsilonHelper.GetEpsilonRange(currentProblem, edge, pointy[i].X, edge.X2, pointy[i].Y, edge.Y2);
                                        if (vals.Item1 == EpsilonHelper.LineState.Okay)
                                        {
                                            closestSuccessfulPoint = pointy[i];
                                            break;
                                        }
                                    }
                                    else
                                    {
                                        break;
                                    }
                                }
                            }


                            if (closestSuccessfulPoint != null)
                            {
                                a.X = closestSuccessfulPoint.Value.X;
                                a.Y = closestSuccessfulPoint.Value.Y;
                                a.Center = closestSuccessfulPoint.Value;
                                a.IsInside = true;
                                updateAction?.Invoke();
                                hadSuccess = true;
                            }
                            else
                            {
                                Point pinpoint = new Point(x, y);
                                if (_polygon.IsPointInPolygon(pinpoint))
                                {
                                    a.X = x;
                                    a.Y = y;
                                    a.Center = pinpoint;
                                    a.IsInside = true;
                                    updateAction?.Invoke();
                                }
                            }
                        }

                        if (!hadSuccess)
                        {
                            Point randomizedPoint = new Point(x, y);
                            if (_polygon.IsPointInPolygon(randomizedPoint))
                            {
                                a.X = x;
                                a.Y = y;
                                a.Center = randomizedPoint;
                                a.IsInside = true;
                                updateAction?.Invoke();
                            }
                        }
                    }, DispatcherPriority.Background);

                    //areAllInBoundary = Shapes.GetEntriesFromListAsType<WrappedLine>()
                    //    .All(opt => opt.IsInbound);
                    //areAllDistancesChecked = Shapes.GetEntriesFromListAsType<WrappedPoint>()
                    //    .All(opt => opt.IsInside);
                }
            }

            exportAction?.Invoke();
        }

        public bool RandomV3(Problem currentProblem, Action updateAction, ObservableCollection<IContestShape> shapes)
        {
            var wrappedPoints = shapes.GetEntriesFromListAsType<WrappedPoint>();
            List<SafetyAutomaticWrapper> allSaves = new List<SafetyAutomaticWrapper>();
            foreach (var wrappedPoint in wrappedPoints)
            {
                allSaves.AddRange(wrappedPoint.GetValidPointsForEdge(shapes, currentProblem));
            }

            foreach (var save in allSaves)
            {
                save.Random();
            }
            updateAction?.Invoke();
            if (shapes.GetEntriesFromListAsType<WrappedLine>().All(x => x.State == EpsilonHelper.LineState.Okay))
                return true;
            return false;
        }
    }
}
