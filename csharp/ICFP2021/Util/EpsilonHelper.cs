using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using ICFP2021.ui.shapes;

namespace ICFP2021.Util
{
    public static class EpsilonHelper
    {
        public static (LineState, double) GetEpsilonRange(Problem p, WrappedLine line)
        {
            return GetEpsilonRange(p, line, line.X, line.X2, line.Y, line.Y2);
        }

        public static (LineState, double) GetEpsilonRange(Problem p, WrappedLine line , double X, double X2, double Y, double Y2)
        {
            var sqr = GetDistance(X, X2, Y, Y2);
            var sqrOld = GetDistance(line.InitX, line.InitX2, line.InitY, line.InitY2);

            double boundary = (double)p.Epsilon / 1_000_000;
            //double boundary = (double)p.Epsilon / 1_000_000;
            var res = (sqr / sqrOld - 1);
            LineState state = LineState.Okay;
            if (Math.Abs(res) <= boundary)
            {
                state = LineState.Okay;
            }
            else
            {
                if (res > 0)
                {
                    state = LineState.TooLong;
                }
                else
                {
                    state = LineState.TooShort;
                }
            }
            return (state, res);
        }

        public static LineState GetEpsilonStateBetweenPoints(Problem p, WrappedPoint from, Point to)
        {
            var sqr = GetDistance(from.X, to.X, from.Y, to.Y);
            var sqrOld = GetDistance(from.Origin.X, to.X, from.Origin.Y, to.Y);
            double boundary = (double)p.Epsilon / 1_000_000;
            var res = (sqr / sqrOld - 1);
            LineState state = LineState.Okay;
            if (Math.Abs(res) <= boundary)
            {
                state = LineState.Okay;
            }
            else
            {
                if (res > 0)
                {
                    state = LineState.TooLong;
                }
                else
                {
                    state = LineState.TooShort;
                }
            }
            return state;
        }

        public static double GetDistance(double x, double x2, double y, double y2)
        {
            return Square(x - x2) + Square(y - y2);
        }

        public static double GetCircleWidth(double x, double x2, double y, double y2)
        {
            return Math.Sqrt(Square(x - x2) + Square(y - y2));
        }

        private static double Square(double a)
        {
            return a*a;
        }

        public enum LineState
        {
            TooLong,
            TooShort,
            Okay
        }

    }
}
