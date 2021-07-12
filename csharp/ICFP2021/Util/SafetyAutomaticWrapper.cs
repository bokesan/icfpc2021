using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using ICFP2021.ui.shapes;

namespace ICFP2021.Util
{
    public class SafetyAutomaticWrapper
    {
        private WrappedPoint sourcePoint;
        private List<SavePoint> saves;
        Random rnd = new Random();
        public SafetyAutomaticWrapper(WrappedPoint target, List<SavePoint> possibles)
        {
            this.sourcePoint = target;
            this.saves = possibles;
        }

        public void Random()
        {
            sourcePoint.Center = saves[rnd.Next(0, saves.Count)].Center;
            sourcePoint.X = sourcePoint.Center.X;
            sourcePoint.Y = sourcePoint.Center.Y;
        }
    }
}
