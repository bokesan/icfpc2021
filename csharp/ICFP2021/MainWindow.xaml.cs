using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Diagnostics.Eventing.Reader;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using System.Windows.Threading;
using ICFP2021.Setup;
using ICFP2021.ui.cmd;
using ICFP2021.ui.shapes;
using ICFP2021.Util;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Path = System.Windows.Shapes.Path;

namespace ICFP2021
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public ScaleTransform Transformer { get; } = new ScaleTransform();

        public Solver Solver { get; private set; }
        public string ProblemNumber { get; set; }
        public double Degrees { get; set; } = 5d;
        public int Steps { get; set; }
        public int Number { get; set; }

        private Problem p;

        public ICommand RandomCmd { get; }

        public MainWindow()
        {
            InitializeComponent();
            RandomCmd = new RelayCommand(DoRandomize);
            DataContext = this;
            Solver = new Solver();
        }

        private void DoRandomize(object obj)
        {
            //Solver.Randomize();
        }

        private void ZoomIn(object sender, RoutedEventArgs e)
        {
            Transformer.ScaleX *= 2;
            Transformer.ScaleY *= 2;
            Solver.Canvas.Height *= 2;
            Solver.Canvas.Width *= 2;
        }

        private void ZoomOut(object sender, RoutedEventArgs e)
        {
            Transformer.ScaleX /= 2;
            Transformer.ScaleY /= 2;
            Solver.Canvas.Height /= 2;
            Solver.Canvas.Width /= 2;
        }


        private void Export(object sender, RoutedEventArgs e)
        {
            Solver.ExportPrint();
        }

        private void Reload(object sender, RoutedEventArgs e)
        {
            ProblemInitializer pi = new ProblemInitializer();
            try
            {
                Problem p = pi.GetProblem($@"C:\Users\DAK\Downloads\{ProblemNumber}.problem");

                Solver.Shapes.Clear();
                Solver.Init(p);
            }
            catch (FileNotFoundException ex)
            {
                //do nothing
            }
        }

        private void LoadSolution()
        {
            Solver.LoadSolution();
        }

        private void Load(object sender, RoutedEventArgs e)
        {
            LoadSolution();
        }

        private void CanvasLoaded(object sender, RoutedEventArgs e)
        {
            if (sender is Canvas c && c.DataContext is MainWindow s)
            {
                c.RenderTransform = Transformer;
                s.Solver.SetCanvas(c);
            }
        }

        private void rectangle_MouseMove(object sender, MouseEventArgs e)
        {
            Solver.rectangle_MouseMove(sender, e);
        }

        private void rectangle_MouseDown(object sender, MouseButtonEventArgs e)
        {
            Solver.rectangle_MouseDown(sender, e);
        }

        private void LockItem(object sender, MouseButtonEventArgs e)
        {
            Solver.LockItem(sender, e);
        }

        private void canvas_mouseDown(object sender, MouseButtonEventArgs e)
        {
            Solver.canvas_mouseDown(sender, e);
        }

        private void MainWindow_OnKeyDown(object sender, KeyEventArgs e)
        {
        }

        private void Left(object sender, RoutedEventArgs e)
        {
            Solver.MoveShapesHorizontal(Steps * -1);
        }

        private void Up(object sender, RoutedEventArgs e)
        {
            Solver.MoveShapesVertical(Steps * -1);
        }

        private void Right(object sender, RoutedEventArgs e)
        {
            Solver.MoveShapesHorizontal(Steps);
        }

        private void Down(object sender, RoutedEventArgs e)
        {
            Solver.MoveShapesVertical(Steps);
        }

        private void ClearSnipping(object sender, RoutedEventArgs e)
        {
            Solver.ClearSnippings();
        }

        private void SelectAll(object sender, RoutedEventArgs e)
        {
            Solver.SelectAll();
        }

        private void Unpack(object sender, RoutedEventArgs e)
        {
            Solver.TryUnpackOfSelection();
        }

        private void Cancel(object sender, RoutedEventArgs e)
        {
            Solver.Cancel();
        }

        private void Rotate(object sender, RoutedEventArgs e)
        {
            Solver.Rotate(Degrees);
        }

        private Polygon currentPolygon;
        private void PolyLoad(object sender, RoutedEventArgs e)
        {
            if (sender is Polygon p)
            {
                currentPolygon = p;
                Solver.InitFromPoly(p);
            }
        }

        

        private void Highlight(object sender, RoutedEventArgs e)
        {
            Solver.Highlight(Number);
        }

        private void CreatePolyPoints(object sender, RoutedEventArgs e)
        {
            Solver.InitFromPoly(currentPolygon);
        }
    }
}