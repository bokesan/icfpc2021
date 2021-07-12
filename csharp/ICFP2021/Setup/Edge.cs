using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ICFP2021
{
    [JsonConverter(typeof(EdgeConverter))]
    public class Edge
    {
        public int A { get; set; }
        public int B { get; set; }
    }
    public class EdgeConverter : JsonConverter
    {

        public override bool CanConvert(Type objectType)
        {
            return objectType.Name.Equals("edge");
        }

        public override object ReadJson(JsonReader reader, Type objectType, object existingValue, JsonSerializer serializer)
        {

            JArray array = JArray.Load(reader);
            return new Edge
            {
                A = array[0].ToObject<int>(),
                B = array[1].ToObject<int>()
            };
        }

        public override void WriteJson(JsonWriter writer, object value, JsonSerializer serializer)
        {

        }
    }
}
