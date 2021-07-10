use geo::{Polygon, Point, LineString};
use serde::{Serialize, Deserialize};
use std::fs::File;
use std::io::Read;

#[derive(Debug)]
pub struct Problem {
    pub hole: Polygon<u32>,
    pub vertices: LineString<u32>,
    pub edges: Vec<(usize,usize)>,
    pub epsilon: u32,
}

pub fn read_problem(filename: String) -> Problem {
    let mut file = File::open(filename).unwrap();
    let mut data = String::new();
    file.read_to_string(&mut data).unwrap();
    let problem: DxProblem = serde_json::from_str(&data).unwrap();
    to_problem(&problem)
}

fn to_problem(dx: &DxProblem) -> Problem {
    Problem { hole: Polygon::new(dx.hole.iter().map(|x|to_point(x)).into_iter().collect(), vec![]),
              vertices: dx.figure.vertices.iter().map(|x| to_point(x)).collect(),
              edges: dx.figure.edges.iter().map(|x| (x[0] as usize, x[1] as usize)).collect(),
              epsilon: dx.epsilon }
}

fn to_point(dx: &Vec<u32>) -> Point<u32> {
    assert_eq!(2, dx.len());
    Point::new(dx[0], dx[1])
}

#[derive(Serialize, Deserialize, Debug)]
struct DxProblem {
    hole: Vec<Vec<u32>>,
    figure: DxFigure,
    epsilon: u32
}

#[derive(Serialize, Deserialize, Debug)]
struct DxFigure {
    edges: Vec<Vec<u32>>,
    vertices: Vec<Vec<u32>>,
}