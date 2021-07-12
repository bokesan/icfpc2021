using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;

namespace ICFP2021.ui.shapes
{
    public class WrappedPoint : ShapeBase, IContestShape
    {
        private double _x;
        private double _x2;
        private double _y;
        private double _y2;
        private bool _isSnapped;
        private SolidColorBrush _brush = new SolidColorBrush(Colors.Aqua);
        private Point _center;
        private bool _isInside;

        public double X
        {
            get => _x;
            set
            {
                if (value == _x) return;
                _x = value;
                OnPropertyChanged();
                OnPropertyChanged(nameof(ToolTip));
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
                OnPropertyChanged(nameof(ToolTip));
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
        public int Index { get; set; }

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

        public Point Center
        {
            get => _center;
            set
            {
                if (value.Equals(_center)) return;
                _center = value;
                OnPropertyChanged();
                OnPropertyChanged("ToolTip");
            }
        }

        public bool IsInside
        {
            get => _isInside;
            set
            {
                if (value == _isInside) return;
                _isInside = value;
                OnPropertyChanged();
            }
        }
        public Point Origin { get; set; }
        public HashSet<WrappedPoint> AttachedTo { get; } = new HashSet<WrappedPoint>();
        public HashSet<WrappedLine> Edges { get; } = new HashSet<WrappedLine>();
        public int Hit { get; set; } = 40;

        public string ToolTip => $"{Index}: X-{Center.X} Y-{Center.Y}";
        public override void Update()
        {
            
        }

        public WrappedPoint(Point origin)
        {
            this.Origin = origin;
        }
    }
}
