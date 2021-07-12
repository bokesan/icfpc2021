using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Shapes;
using ICFP2021.ui.shapes;

namespace ICFP2021.ui
{
    class CanvasItemStyleSelector : StyleSelector
    {
        public Style LineStyle { get; set; }
        public Style DefaultStyle { get; set; }
        public override Style SelectStyle(object item, DependencyObject container)
        {
            if (item is WrappedLine line)
                return LineStyle;
            else if (item is WarningShape)
            {
                return LineStyle;
            }
            else if (item is WrappedPoint)
            {
                return LineStyle;
            }
            else if (item is SavePoint)
            {
                return LineStyle;
            }
            else
            {
                return DefaultStyle;
            }
            return base.SelectStyle(item, container);
        }
    }
}
