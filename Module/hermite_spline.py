import math
import matplotlib.pyplot as plot
import numpy as np


class HermiteSpline:
    class Point:
        T = 0.0
        X = 0.0
        M = 0.0

    def FindIdx(self, t, idx_prev):
        idx = idx_prev
        if idx >= len(self.KeyPts):
            idx = len(self.KeyPts) - 1
        while idx+1 < len(self.KeyPts) and t > self.KeyPts[idx + 1].T:
            idx += 1
        while idx >= 0 and t < self.KeyPts[idx].T:
            idx -= 1
        return idx

    # Return interpolated value at t
    def Evaluate(self, t):
        idx = self.FindIdx(t, 0)
        if abs(t - self.KeyPts[-1].T) < 1.0e-6:
            idx = len(self.KeyPts) - 2
        if idx < 0 or idx >= len(self.KeyPts) - 1:
            if idx < 0:
                idx = 0
                t = self.KeyPts[0].T
            else:
                idx = len(self.KeyPts) - 2
                t = self.KeyPts[-1].T

        # Hermite spline polynomial formulas
        def h00(t): return t * t * (2.0 * t - 3.0) + 1.0
        def h10(t): return t * (t * (t - 2.0) + 1.0)
        def h01(t): return t * t * (-2.0 * t + 3.0)
        def h11(t): return t * t * (t - 1.0)

        self.idx_prev = idx
        p0 = self.KeyPts[idx]
        p1 = self.KeyPts[idx + 1]
        tr = (t - p0.T) / (p1.T - p0.T)
        # Spline polynomial main formula
        return h00(tr) * p0.X + h10(tr) * (p1.T - p0.T) * p0.M + h01(tr) * p1.X + h11(tr) * (p1.T - p0.T) * p1.M


    def Calculate(self, data):
        if data != None:
            self.KeyPts = [self.Point() for i in range(len(data))]
            for idx in range(len(data)):
                self.KeyPts[idx].T = data[idx][0]
                self.KeyPts[idx].X = data[idx][1]

        # First derivative formula
        def grad(idx1, idx2): return (
            self.KeyPts[idx2].X - self.KeyPts[idx1].X) / (self.KeyPts[idx2].T - self.KeyPts[idx1].T)

        for idx in range(1, len(self.KeyPts)-1):
            self.KeyPts[idx].M = grad(idx - 1, idx + 1)

        self.KeyPts[0].M = grad(0, 1)
        self.KeyPts[-1].M = grad(-2, -1)


if __name__ == "__main__":

    points = [
        [1.0, 1.0],
        [2.0, 1.0],
        [3.0, 2.0],
        [4.0, 4.0],
        [5.0, 1.0],
        [6.0, 4.0],
        [7.0, 5.0],
        [9.0, 1.0],
        [10.0, 2.0]
    ]
    points.sort()

    plot.plot([p[0] for p in points], [p[1] for p in points], 'o')

    spline = HermiteSpline()
    spline.Calculate(points)

    X = []
    Y = []
    for t in np.arange(points[0][0], points[-1][0], 0.001):
        x = spline.Evaluate(t)
        X.append(t)
        Y.append(x)

    plot.title("Сплайн Ерміта")

    plot.plot(X, Y)
    plot.show()
