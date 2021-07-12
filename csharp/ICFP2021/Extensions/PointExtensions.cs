using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using ICFP2021.ui.shapes;
using ICFP2021.Util;

namespace ICFP2021.Extensions
{
    static class PointExtensions
    {
        public static double GetAngle(this Point p, Point target)
        {
            double rad2deg = 180 / Math.PI;
            double deg2rad = Math.PI / 180;

            return Math.Atan2(p.Y - target.Y, p.X - target.X) * rad2deg;
        }
        public static Point GetPointFromAngle(Point point, double angle, long distance)
        {
            return new Point();

            //    point[0] + (Math.sin(angle) * distance),
            //point[1] - (Math.cos(angle) * distance)
        }

        public static void Rotate(this WrappedPoint pointToRotate, WrappedPoint centerPoint, double angleInDegrees)
        {
            double angleInRadians = angleInDegrees * (Math.PI / 180);
            double cosTheta = Math.Cos(angleInRadians);
            double sinTheta = Math.Sin(angleInRadians);
            double x = (cosTheta * (pointToRotate.X - centerPoint.X) - sinTheta * (pointToRotate.Y - centerPoint.Y) + centerPoint.X);
            double y = (sinTheta * (pointToRotate.X - centerPoint.X) + cosTheta * (pointToRotate.Y - centerPoint.Y) + centerPoint.Y);

            Point p = new Point(x, y);
            pointToRotate.Center = p;
            pointToRotate.X = pointToRotate.Center.X;
            pointToRotate.Y = pointToRotate.Center.Y;
        }

        public static void MarkValidPoints(this WrappedPoint wrappedPoint, ObservableCollection<IContestShape> shapes, Problem problem)
        {
            
        }

        public static List<SafetyAutomaticWrapper> GetValidPointsForEdge(this WrappedPoint wrappedPoint,
            ObservableCollection<IContestShape> shapes, Problem problem)
        {
            var savePoints = shapes.GetEntriesFromListAsType<SavePoint>();
            List<SafetyAutomaticWrapper> edgesWithPossibles = new List<SafetyAutomaticWrapper>();
            foreach (var edge in wrappedPoint.Edges)
            {
                List<SavePoint> save = new List<SavePoint>();
                foreach (var savepoint in savePoints)
                {
                    bool isGood = EpsilonHelper.GetEpsilonRange(problem, edge, wrappedPoint.X, savepoint.X,
                        wrappedPoint.Y, savepoint.Y).Item1 == EpsilonHelper.LineState.Okay;
                    if(isGood) save.Add(savepoint);
                }
                edgesWithPossibles.Add(new SafetyAutomaticWrapper(wrappedPoint, save));
            }

            return edgesWithPossibles;
        }
    }
}
