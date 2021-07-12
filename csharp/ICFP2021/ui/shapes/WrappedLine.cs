using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;
using ICFP2021.Util;

namespace ICFP2021.ui.shapes
{
    public class WrappedLine : ShapeBase, IContestShape
    {
        private double _x;
        private double _x2;
        private double _y;
        private double _y2;

        public readonly double InitX;
        public readonly double InitX2;
        public readonly double InitY;
        public readonly double InitY2;
        public double Distance = 0;
        private bool _isInbound = false;
        private SolidColorBrush _brush;
        private string _value;
        public int IndexA { get; set; }
        public int IndexB { get; set; }

        public Color DefaultColor { get; set; }

        public WrappedLine(double startX, double startY, double endX, double endY, Random rnd)
        {
            X = startX;
            Y = startY;
            X2 = endX;
            Y2 = endY;

            InitX = startX;
            InitY = startY;
            InitX2 = endX;
            InitY2 = endY;
            DefaultColor = Color.FromRgb((byte)rnd.Next(0, 100),(byte) rnd.Next(0, 255),(byte) rnd.Next(0, 255));

        }

        public Edge BaseEdge { get; set; }

        public double X
        {
            get => _x;
            set
            {
                if (value == _x) return;
                _x = value;
                OnPropertyChanged();
            }
        }

        public double X2
        {
            get => _x2;
            set
            {
                if (value == _x2) return;
                _x2 = value;
                OnPropertyChanged();
            }
        }

        public double Y
        {
            get => _y;
            set
            {
                if (value == _y) return;
                _y = value;
                OnPropertyChanged();
            }
        }

        public double Y2
        {
            get => _y2;
            set
            {
                if (value == _y2) return;
                _y2 = value;
                OnPropertyChanged();
            }
        }

        public double Z { get; set; }
        public Type BaseType { get; set; }

        public bool IsInbound
        {
            get => _isInbound;
            set
            {
                if (value == _isInbound) return;
                _isInbound = value;
                OnPropertyChanged();
            }
        }

        public SolidColorBrush Brush
        {
            get => _brush;
            set
            {
                if (Equals(value, _brush)) return;
                _brush = value;
                OnPropertyChanged();
            }
        }

        public string Value
        {
            get => _value;
            set
            {
                if (value == _value) return;
                _value = value;
                OnPropertyChanged();
            }
        }

        public bool IsSnapped
        {
            get => _isSnapped;
            set
            {
                if (value == _isSnapped) return;
                _isSnapped = value;
                OnPropertyChanged();
            }
        }

        public EpsilonHelper.LineState State;
        private bool _isSnapped;

        public void Check(Problem p)
        {
            (EpsilonHelper.LineState, double) state = EpsilonHelper.GetEpsilonRange(p, this);
            State = state.Item1;
            switch (state.Item1)
            {
                case EpsilonHelper.LineState.Okay:
                    Brush = new SolidColorBrush(DefaultColor);
                    IsInbound = true;
                    break;
                case EpsilonHelper.LineState.TooLong:
                    Brush = new SolidColorBrush(Colors.Red);
                    IsInbound = false;
                    break;
                case EpsilonHelper.LineState.TooShort:
                    Brush = new SolidColorBrush(Colors.Yellow);
                    IsInbound = false;
                    break;
            }

            Value = state.Item2.ToString();
        }

        public override void Update()
        {
            
        }

        public Point[] GetPoints(int quantity)
        {
            var points = new Point[quantity];
            int ydiff = (int) (Y2 - Y), xdiff = (int) (X2 - X);
            double slope = (double)(Y2 - Y) / (X2 - X);
            double x, y;

            --quantity;

            for (double i = 0; i < quantity; i++)
            {
                y = slope == 0 ? 0 : ydiff * (i / quantity);
                x = slope == 0 ? xdiff * (i / quantity) : y / slope;
                points[(int)i] = new Point((int)Math.Round(x) + X, (int)Math.Round(y) + Y);
            }

            points[quantity] = new Point(X2, Y2);
            return points;
        }

        public Point LineCalculation(Point p)
        {
            var a = Y2 - Y;
            var b = -(X2 - X);
            var c = -a * X - b * Y;

            var m = Math.Sqrt(a * a + b * b);
            var a2 = a / m;
            var b2 = b / m;
            var c2 = c / m;

            var d = a2 * p.X + b2 * p.Y + c2;

            var px = p.X - 2 * a2 * d;
            var py = p.Y - 2 * b2 * d;

            return new Point(Math.Round(px, MidpointRounding.ToEven), Math.Round(py, MidpointRounding.ToEven));
        }
    }
}
