package decision_trees;

class DataPoint {
    double x1;
    double x2;
    double y;

    public DataPoint(double x1, double x2, double y) {
        this.x1 = x1;
        this.x2 = x2;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x1 + ", " + x2 + ", " + y + ")";
    }
}