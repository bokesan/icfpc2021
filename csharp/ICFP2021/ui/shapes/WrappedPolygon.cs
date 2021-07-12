using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;
using System.Windows.Shapes;
using ICFP2021.Extensions;

namespace ICFP2021.ui.shapes
{
    public class WrappedPolygon : IContestShape
    {
        public double X { get; set; }
        public double X2 { get; set; }
        public double Y { get; set; }
        public double Y2 { get; set; }
        public double Z { get; set; }
        //defines the edges
        public PointCollection Points { get; set; } = new PointCollection();
        public Type BaseType { get; set; }
        public PointCollection SavePoints { get; set; } = new PointCollection();
        public WrappedPolygon(IList<Hole> edges)
        {
            foreach (var e in edges)
            {
                Points.Add(new Point(e.X, e.Y));
            }
        }
    }
}
