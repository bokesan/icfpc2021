using System;
using System.CodeDom;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Automation.Peers;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Media3D;
using System.Windows.Shapes;
using System.Windows.Threading;
using ICFP2021.Annotations;
using ICFP2021.Extensions;
using ICFP2021.ui.shapes;
using ICFP2021.Util;
using Newtonsoft.Json;
using Path = System.Windows.Shapes.Path;

namespace ICFP2021
{
    public class Solver : INotifyPropertyChanged
    {
        public ICollectionView Source { get; }
        public SupressableCollection<IContestShape> Shapes { get; } = new SupressableCollection<IContestShape>();
        private WrappedPolygon _poly;
        private Problem _currentProblem;
        private DragDropBehavior _dragDropBehavior;
        public string Dislikes
        {
            get => _dislikes;
            set
            {
                if (value == _dislikes) return;
                _dislikes = value;
                OnPropertyChanged();
            }
        }


        public Solver()
        {
            _dragDropBehavior = new DragDropBehavior(Shapes);
            Source = CollectionViewSource.GetDefaultView(Shapes);
        }

        public void Init(Problem p)
        {
            this._currentProblem = p;
            int index = 0;

            InitHole(p.Hole);
            foreach (var vertice in p.Figure.Vertices)
            {
                InitPoint(vertice.X, vertice.Y, index);
                index = index + 1;
            }

            foreach (var edge in p.Figure.Edges)
            {
                InitEdge(edge);
            }
        }

        public void InitSavePoints()
        {
            Shapes.SupressNotification = true;
            if (_poly != null)
                {
                    int count = 0;
                    
                    foreach (var savePoint in _poly.SavePoints)
                    {

                        SavePoint sv = new SavePoint();
                        sv.X = savePoint.X;
                        sv.Y = savePoint.Y;
                        sv.Z = 60;
                        sv.Center = savePoint;
                        sv.BaseType = typeof(SavePoint);
                        
                        Shapes.Add(sv);
                        
                        count = count + 1;
                        
                        Debug.WriteLine($"{count}/{_poly.SavePoints.Count} | {((double)count / (double)_poly.SavePoints.Count * 100d)}%");
                    }
                }

            Shapes.SupressNotification = false;
            Source.Refresh();
        }

        public void InitHole(IList<Hole> pHole)
        {
            WrappedPolygon p = new WrappedPolygon(pHole);
            p.Z = 55;
            p.BaseType = typeof(WrappedPolygon);
            _poly = p;
            Shapes.Add(p);
        }

        public void InitPoint(int x, int y, int index)
        {
            WrappedPoint p = new WrappedPoint(new Point(x,y));
            p.X = x;
            p.Y = y;
            p.Index = index;
            p.Z = 300;
            p.Center = new Point(x, y);
            p.BaseType = typeof(WrappedPoint);
            p.IsInside = _poly.IsPointInPolygon(p.Center);
            Shapes.Add(p);
        }
        
        public void InitEdge(Edge edge)
        {
            var start = Shapes.GetEntriesFromListAsType<WrappedPoint>()
                .Single(s => s.Index == edge.A);
            var end = Shapes.GetEntriesFromListAsType<WrappedPoint>()
                .Single(s => s.Index == edge.B);
            start.AttachedTo.Add(end);
            end.AttachedTo.Add(start);

            WrappedLine l = new WrappedLine(start.X, start.Y, end.X, end.Y, rnd);
            start.Edges.Add(l);
            end.Edges.Add(l);
            l.BaseEdge = edge;
            l.Z = 250;
            l.IndexA = edge.A;
            l.IndexB = edge.B;
            l.Check(_currentProblem);
            l.Distance = EpsilonHelper.GetDistance(l.X, l.X2, l.Y, l.Y2);
            l.BaseType = typeof(WrappedLine);
            Shapes.Add(l);
        }

        public void UpdateEdges()
        {
            foreach (var edge in _currentProblem.Figure.Edges)
            {
                var start = Shapes.GetEntriesFromListAsType<WrappedPoint>()
                    .Single(s => s.Index == edge.A);
                var end = Shapes.GetEntriesFromListAsType<WrappedPoint>()
                    .Single(s => s.Index == edge.B);
                var hit = Shapes.GetEntriesFromListAsType<WrappedLine>()
                    .Single(e => e.BaseEdge == edge);

                hit.X = start.X;
                hit.Y = start.Y;
                hit.X2 = end.X;
                hit.Y2 = end.Y;
                hit.Distance = EpsilonHelper.GetDistance(hit.X, hit.X2, hit.Y, hit.Y2);
                hit.Check(_currentProblem);
            }
            CalcDislikes();
        }

        Random rnd = new Random();

        


        public Canvas Canvas { get; set; }

        public void rectangle_MouseDown(object sender, MouseButtonEventArgs e)
        {
            if (Keyboard.IsKeyDown(Key.LeftCtrl) && sender is Path elip && elip.DataContext is WrappedPoint flipPoint)
            {
                DoFlip(flipPoint);
                return;
            }else if (Keyboard.IsKeyDown(Key.LeftAlt) && sender is Path p1 && p1.DataContext is WrappedPoint target)
            {
                MarkAllValidPoints(target);
                return;
            }


            if (_dragDropBehavior.IsDragActive && sender is Path pa && (pa.DataContext is WrappedPoint wrap || pa.DataContext is SavePoint sp))
            {
                _dragDropBehavior.Move(UpdateEdges, pa.DataContext as IContestShape);
                _dragDropBehavior.SetDrag(false);
                _dragDropBehavior.SetPoint(null);
                return;
            }

            if (sender is Path el && el.DataContext is WrappedPoint wp)
            {
                // start dragging
                _dragDropBehavior.SetDrag(true);
                // save start point of dragging
                _dragDropBehavior.SetStartPoint(Mouse.GetPosition(Canvas));
                _dragDropBehavior.SetPoint(wp);
            }

            blockCanvasEvent = true;
        }

        private bool blockCanvasEvent = false;
        private void DoFlip(WrappedPoint flipPoint)
        {
            var line = Shapes.GetEntriesFromListAsType<WrappedLine>().SingleOrDefault(o => o.IsSnapped);
            if (line == null)
                return;
            var pp = line.LineCalculation(flipPoint.Center);
            flipPoint.Center = pp;
            flipPoint.X = pp.X;
            flipPoint.Y = pp.Y;
            line.IsSnapped = false;
            UpdateEdges();
        }

        private string _dislikes;

        public void rectangle_MouseMove(object sender, MouseEventArgs e)
        {
            //_dragDropBehavior.Move(Canvas, sender, new Action(UpdateEdges));
        }

        public void LockItem(object sender, MouseButtonEventArgs e)
        {
            if (sender is Path el && el.DataContext is WrappedPoint p)
            {
                p.IsSnapped = !p.IsSnapped;
                if (p.IsSnapped)
                {
                    p.Brush = new SolidColorBrush(Colors.Blue);
                }
                else
                {
                    p.Brush = new SolidColorBrush(Colors.Aqua);
                }
            }

            if (sender is Line l && l.DataContext is WrappedLine wl)
            {
                wl.IsSnapped = !wl.IsSnapped;
                if (wl.IsSnapped)
                {
                    wl.Brush = new SolidColorBrush(Colors.Blue);
                }
                else
                {
                    wl.Check(this._currentProblem);
                }
            }
        }

        public void ExportPrint()
        {
            var hits = Shapes.GetEntriesFromListAsType<WrappedPoint>().OrderBy(x => x.Index);
            StringBuilder sb = new StringBuilder();
            sb.Append("{");
            sb.Append("" + '"' + "vertices" + '"' + ": [");
            foreach (var point in hits)
            {
                sb.Append($"[{Math.Floor(point.Center.X)},{Math.Floor(point.Center.Y)}],");
            }

            sb.Remove(sb.Length - 1, 1);
            sb.Append("]}");
            var val = sb.ToString();
            Clipboard.SetText(val);
            MessageBox.Show(val);
        }

        public void SetCanvas(Canvas canvas)
        {
            Canvas = canvas;
            this.Canvas.MouseLeftButtonDown += canvas_mouseDown;
        }

        public void canvas_mouseDown(object sender, MouseButtonEventArgs e)
        {
            if (blockCanvasEvent)
            {
                blockCanvasEvent = false;
                return;
            }

            if (_dragDropBehavior.IsDragActive && (sender is Canvas || sender is Window))
            {
                var pos = Mouse.GetPosition(Canvas);
                _dragDropBehavior.Move(UpdateEdges, new SavePoint()
                {
                    Center = new Point(Math.Round(pos.X, MidpointRounding.ToEven), Math.Round(pos.Y, MidpointRounding.ToEven))
                });
                _dragDropBehavior.SetDrag(false);
                _dragDropBehavior.SetPoint(null);
                return;
            }
        }


        public void LoadSolution()
        {
            var t = JsonConvert.DeserializeObject<Problem>(File.ReadAllText(@"C:\Users\DAK\Desktop\icfp\7.json"));
            var targets = Shapes.GetEntriesFromListAsType<WrappedPoint>().ToList();
            for (int i = 0; i < t.Figure.Vertices.Count; i++)
            {
                var p = t.Figure.Vertices[i];
                targets[i].X = p.X;
                targets[i].Y = p.Y;
                targets[i].Center = new Point(p.X, p.Y);
            }

            UpdateEdges();
        }

        public void MoveShapesVertical(int y)
        {
            foreach (var pointToMove in Shapes.GetEntriesFromListAsType<WrappedPoint>().Where(x => !x.IsSnapped))
            {
                var center = pointToMove.Center;
                pointToMove.Center = new Point(center.X, center.Y + y);
                pointToMove.X = pointToMove.Center.X;
                pointToMove.Y = pointToMove.Center.Y;
            }
            UpdateEdges();
        }

        public void MoveShapesHorizontal(int xAxis)
        {
            foreach (var pointToMove in Shapes.GetEntriesFromListAsType<WrappedPoint>().Where(x => !x.IsSnapped))
            {
                var center = pointToMove.Center;
                pointToMove.Center = new Point(center.X+xAxis, center.Y);
                pointToMove.X = pointToMove.Center.X;
                pointToMove.Y = pointToMove.Center.Y;
            }
            UpdateEdges();
        }

        public void CalcDislikes()
        {

            int n = _currentProblem.Hole.Count();
            int m = _currentProblem.Figure.Vertices.Count;
            long sum = 0;
            var vertices = Shapes.GetEntriesFromListAsType<WrappedPoint>().ToList();
            for (int i = 0; i < n; i++)
            {
                long minDistance = long.MaxValue;
                for (int k = 0; k < m; k++)
                {
                    minDistance = Math.Min(minDistance, _currentProblem.Hole[i].DistanceSquared(vertices[k].Center));
                }
                sum += minDistance;
            }

            Dislikes = sum.ToString();
        }

        public event PropertyChangedEventHandler PropertyChanged;

        [NotifyPropertyChangedInvocator]
        protected virtual void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        public void ClearSnippings()
        {
            foreach (var shape in Shapes)
            {
                switch (shape)
                {
                    case WrappedPoint wp:
                        wp.IsSnapped = false;
                        wp.Brush = new SolidColorBrush(Colors.Aqua);
                        break;
                    case WrappedLine wl:
                        wl.IsSnapped = false;
                        wl.Brush = new SolidColorBrush(Colors.Black);
                        break;
                    default:
                        break;
                }
            }
            UpdateEdges();
        }

        public void SelectAll()
        {
            foreach (var shape in Shapes)
            {
                switch (shape)
                {
                    case WrappedPoint wp:
                        wp.IsSnapped = true;
                        wp.Brush = new SolidColorBrush(Colors.Blue);
                        break;
                    default:
                        break;
                }
            }
        }

        private bool _isNotCancel = true;
        public void TryUnpackOfSelection()
        {
            Automatics a = new Automatics(Shapes.GetEntriesFromListAsType<WrappedPoint>(), _poly);
            while (_isNotCancel)
            {
                Application.Current.Dispatcher.Invoke(() =>
                {
                    _isNotCancel = !a.RandomV3(_currentProblem, new Action(UpdateEdges), Shapes);
                }, DispatcherPriority.Background);
            }

            _isNotCancel = true;
        }

        public void Cancel()
        {
            _isNotCancel = false;
        }

        public void Rotate(double degrees)
        {
            var wrappedPoints = Shapes.GetEntriesFromListAsType<WrappedPoint>().ToList();
            if (wrappedPoints.Count(o => o.IsSnapped) != 1)
                return;

            WrappedPoint axisPoint = wrappedPoints.Single(o => o.IsSnapped);
            foreach (var wrappedPoint in wrappedPoints.Where(x => x != axisPoint))
            {
                wrappedPoint.Rotate(axisPoint, degrees);
            }
            UpdateEdges();
        }

        private void MarkAllValidPoints(WrappedPoint wrappedPoint)
        {
            var polygon = Shapes.GetEntriesFromListAsType<WrappedPolygon>().Single();
            var oldPoints = Shapes.GetEntriesFromListAsType<SavePoint>().ToList();
            foreach (var oldPoint in oldPoints)
            {
                Shapes.Remove(oldPoint);
            }

            foreach (var savePoint in polygon.SavePoints)
            {
                foreach (var edge in wrappedPoint.Edges)
                {
                    bool isGood = EpsilonHelper.GetEpsilonRange(_currentProblem, edge, wrappedPoint.X, savePoint.X,
                        wrappedPoint.Y, savePoint.Y).Item1 == EpsilonHelper.LineState.Okay;
                    if (isGood)
                    {
                        SavePoint sp = new SavePoint();
                        sp.Center = savePoint;
                        sp.X = sp.Center.X;
                        sp.BaseType = typeof(SavePoint);
                        sp.Y = sp.Center.Y;
                        sp.Z = 60;
                        sp.Mark(true, edge.DefaultColor);
                        Shapes.Add(sp);
                    }
                }
            }
        }

        public WrappedPolygon GetPolygon()
        {
            return _poly;
        }

        public void Highlight(int number)
        {
            Shapes.GetEntriesFromListAsType<WrappedPoint>().First(o => o.Index == number).Brush = new SolidColorBrush(Colors.Red);
        }

        public void InitFromPoly(Polygon p)
        {
                var l = GetPoints(p.RenderedGeometry, _poly);
                _poly.SavePoints.Clear();
                foreach (var point in l)
                {
                    {
                        _poly.SavePoints.Add(point);
                    }
                }
                //InitSavePoints();

        }
        public List<Point> GetPoints(Geometry geom, WrappedPolygon wrap)
        {
            List<Point> points = new List<Point>();

            var x = wrap.Points.Max(o => o.X);
            var y = wrap.Points.Max(o => o.Y);

            for (int i1 = 0; i1 <= x; i1++)
            for (int i2 = 0; i2 <= y; i2++)
                if (geom.FillContains(new Point(i1, i2)))
                    points.Add(new Point(i1, i2));

            return points;
        }
    }
}