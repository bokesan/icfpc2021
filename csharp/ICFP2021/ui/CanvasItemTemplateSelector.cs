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
    public class CanvasItemTemplateSelector : DataTemplateSelector
    {
        public DataTemplate PointTemplate { get; set; }
        public DataTemplate LineTemplate { get; set; }
        public DataTemplate BackgroundTemplate { get; set; }
        public DataTemplate WarningCircle { get; set; }
        public DataTemplate SavePoint { get; set; }

        public override DataTemplate SelectTemplate(object item, DependencyObject container)
        {
            switch (item)
            {
                case WrappedPoint e:
                    return PointTemplate;
                case WrappedLine l:
                    return LineTemplate;
                case WrappedPolygon p:
                    return BackgroundTemplate;
                case WarningShape w:
                    return WarningCircle;
                case shapes.SavePoint sv:
                    return SavePoint;
            }

            return base.SelectTemplate(item, container);
        }
    }
}
