using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Shapes;
using ICFP2021.Extensions;
using ICFP2021.ui.shapes;

namespace ICFP2021.Util
{
    class DragDropBehavior
    {
        private readonly ObservableCollection<IContestShape> _shapes;
        private bool _drag = false;
        //the currently dragged point
        private WrappedPoint _point;
        private Point? _startPoint;
        private double x;
        private double y;
        public DragDropBehavior(ObservableCollection<IContestShape> shapes)
        {
            this._shapes = shapes;
        }
        public void Move(Canvas canvas, object sender, Action updateAction)
        {
            // if dragging, then adjust rectangle position based on mouse movement
            if (_drag && _point != null && (sender is Canvas || sender is Window))
            {
                var pos = Mouse.GetPosition(canvas);
                var snappedTo = _point.AttachedTo.Where(x => x.IsSnapped);
                var lines = _shapes.Where(x => x.BaseType == typeof(Line)).Cast<WrappedLine>();
                
                    x = Math.Floor(pos.X);
                    y = Math.Floor(pos.Y);
                    var xDifference = _point.X - x;
                    var yDifference = _point.Y - y;
                    if (_point.IsSnapped)
                    {
                        var otherPoints = _shapes.GetEntriesFromListAsType<WrappedPoint>().Where(x => x.IsSnapped && x != _point);
                        foreach (var moved in otherPoints)
                        {
                            moved.X = moved.X - xDifference;
                            moved.Y = moved.Y - yDifference;
                            moved.Center = new Point(moved.X, moved.Y);
                        }
                        _point.X = x;
                        _point.Y = y;
                        _point.Center = new Point(x, y);
                    }
                    else
                    {
                        _point.X = x;
                        _point.Y = y;
                        _point.Center = new Point(x, y);
                    }
                    updateAction?.Invoke();
                    return;
            }

            if (_drag && sender is Path el && el.DataContext is WrappedPoint wp && _point == wp)
            {
                Point newPoint = Mouse.GetPosition(canvas);
                double left = Canvas.GetLeft(el);
                double top = Canvas.GetTop(el);
                x = left + (newPoint.X - _startPoint.Value.X);
                y = top + (newPoint.Y - _startPoint.Value.Y);
                Canvas.SetLeft(el, x);
                Canvas.SetTop(el, y);
                _startPoint = newPoint;
            }
        }

        public void Move(Action updateAction, IContestShape target)
        {
            if (_point == null)
                return;
            Point? center = null;
            if (target is WrappedPoint wp)
            {
                center = wp.Center;
            }else if (target is SavePoint sp)
            {
                center = sp.Center;
            }

            if (center.HasValue)
            {
                x = center.Value.X;
                y = center.Value.Y;
                if (_point.IsSnapped)
                {
                    var otherPoints = _shapes.GetEntriesFromListAsType<WrappedPoint>().Where(x => x.IsSnapped && x != _point);
                    var xDifference = _point.X - x;
                    var yDifference = _point.Y - y;
                    foreach (var moved in otherPoints)
                    {
                        moved.X = moved.X - xDifference;
                        moved.Y = moved.Y - yDifference;
                        moved.Center = new Point(moved.X, moved.Y);
                    }
                    _point.X = x;
                    _point.Y = y;
                    _point.Center = new Point(x, y);
                }
                else
                {
                    _point.X = x;
                    _point.Y = y;
                    _point.Center = new Point(x, y);
                }
            }


            updateAction?.Invoke();
        }


        public void SetDrag(bool isDrag)
        {
            _drag = isDrag;
        }

        public void SetStartPoint(Point? startPoint)
        {
            _startPoint = startPoint;
        }

        public bool IsDragActive => _drag;

        public void SetPoint(WrappedPoint point)
        {
            this._point = point;
        }
    }
}
