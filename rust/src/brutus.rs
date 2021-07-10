use crate::problem::Problem;
use geo::{LineString, Point, Coordinate};
use geo::prelude::BoundingRect;
use geo::contains::Contains;

pub fn solve(problem: &mut Problem) -> Vec<Vec<i32>> {
    let bounds = problem.hole.bounding_rect().unwrap();

    let orig_distances: Vec<u32> = problem.edges.iter()
        .map(|v| dist_sq(&problem.vertices[v.0], &problem.vertices[v.1]))
        .collect();



    let mut candidates = Vec::new();
    for x in bounds.min().x .. bounds.max().x {
        for y in bounds.min().y .. bounds.max().y {
            let p = Point::new(x, y);
            if problem.hole.contains(&p) {
                candidates.push(p);
            }
        }
    }



    vec![]
}

fn dist_sq(p1: &Coordinate<u32>, p2: &Coordinate<u32>) -> u32 {
    let dx = p1.x - p2.x;
    let dy = p1.y - p2.y;
    dx * dx + dy * dy
}