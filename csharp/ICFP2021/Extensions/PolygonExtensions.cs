using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using ICFP2021.ui.shapes;

namespace ICFP2021.Extensions
{
    public static class PolygonExtensions
    {
        public static bool IsPointInPolygon(this WrappedPolygon polygon, Point pointToCheck)
        {
            return polygon.SavePoints.Contains(pointToCheck);
        }

    }
}
