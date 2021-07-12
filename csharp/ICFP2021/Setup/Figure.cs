using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ICFP2021
{
    public class Figure
    {
        public IList<Edge> Edges { get; set; } = new List<Edge>();
        public IList<Vertices> Vertices { get; set; } = new List<Vertices>();
    }
}
