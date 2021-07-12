using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace ICFP2021.ui.shapes
{
    class WarningShape : ShapeBase, IContestShape
    {
        private double _x;
        private double _y;

        public double X
        {
            get => _x;
            set
            {
                if (value.Equals(_x)) return;
                _x = value;
                OnPropertyChanged();
                OnPropertyChanged(nameof(ToolTip));
            }
        }

        public double X2 { get; set; }

        public double Y
        {
            get => _y;
            set
            {
                if (value.Equals(_y)) return;
                _y = value;
                OnPropertyChanged();
                OnPropertyChanged(nameof(ToolTip));
            }
        }

        public double Y2 { get; set; }
        public double Z { get; set; }
        public double Width { get; set; }
        public double Height { get; set; }
        public Type BaseType { get; set; }
        public string ToolTip => $"X:{X} Y:{Y}";

        public Point Center { get; set; }
        public override void Update()
        {
            
        }
    }
}
