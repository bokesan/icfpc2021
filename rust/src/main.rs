use std::env;

mod problem;
mod brutus;

fn main() {
    let args: Vec<String> = env::args().collect();
    let problem = problem::read_problem(args[1].to_owned());
    println!("{:?}", problem);
}
