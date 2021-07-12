using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ICFP2021
{
    public class Problem
    {
        public Figure Figure { get; set; }
        public IList<Hole> Hole { get; set; } = new List<Hole>();
        public long Epsilon { get; set; }
    }
}
