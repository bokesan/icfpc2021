using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ICFP2021.ui.shapes;

namespace ICFP2021.Extensions
{
    public static class CollectonExtension
    {
        public static IEnumerable<T> GetEntriesFromListAsType<T>(this ObservableCollection<IContestShape> shapes) where T : IContestShape
        {
            return shapes.Where(x => x.BaseType == typeof(T)).Cast<T>();
        }
        public static IEnumerable<T> GetEntriesFromListAsType<T>(this IEnumerable<IContestShape> shapes) where T : IContestShape
        {
            return shapes.Where(x => x.BaseType == typeof(T)).Cast<T>();
        }

    }
}
