using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ICFP2021
{
    [JsonConverter(typeof(HoleConverter))]
    public class Hole
    {
        public int X { get; set; }
        public int Y { get; set; }

        public long DistanceSquared(Point q)
        {
            return (long) (Square(X - q.X) + Square(Y - q.Y));
        }
        private static double Square(double a)
        {
            return a * a;
        }
    }

    public class HoleConverter : JsonConverter
    {

        public override bool CanConvert(Type objectType)
        {
            return objectType.Name.Equals("Hole");
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {

            JArray array = JArray.Load(reader);
            return new Hole
            {
               X = array[0].ToObject<int>(),
               Y = array[1].ToObject<int>()
            };
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {
           
        }
    }
}
