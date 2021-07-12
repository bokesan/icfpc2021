using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ICFP2021
{
    [JsonConverter(typeof(VerticesConverter))]
    public class Vertices
    {
        public int X { get; set; }
        public int Y { get; set; }
    }

    public class VerticesConverter : JsonConverter
    {

        public override bool CanConvert(Type objectType)
        {
            return objectType.Name.Equals("vertices");
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {

            JArray array = JArray.Load(reader);
            return new Vertices()
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
