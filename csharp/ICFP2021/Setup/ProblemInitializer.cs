using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace ICFP2021.Setup
{
    class ProblemInitializer
    {
        public Problem GetProblem(string path)
        {
            Problem p = JsonConvert.DeserializeObject<Problem>(File.ReadAllText(path));
            return p;
        }
    }
}
