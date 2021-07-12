using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;

namespace ICFP2021.ui.shapes
{
    public class SavePoint : ShapeBase, IContestShape
    {
        private SolidColorBrush _brush = new SolidColorBrush(Colors.Tan);
        public double X { get; set; }
        public double X2 { get; set; }
        public double Y { get; set; }
        public double Y2 { get; set; }
        public double Z { get; set; }
        public Type BaseType { get; set; }
        public Point Center { get; set; }
        public string ToolTip => $"{Center.X}/{Center.Y}";

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

        public override void Update()
        {
            
        }

        public void Mark(bool shouldMark, Color color)
        {
            Brush = new SolidColorBrush(color);
        }
    }
}
