using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Shapes;

namespace ICFP2021.ui.shapes
{
    public interface IContestShape
    {
        double X { get; set; }
        double X2 { get; set; }
        double Y { get; set; }
        double Y2 { get; set; }
        double Z { get; set; }
        Type BaseType { get; set; }
    }
}
