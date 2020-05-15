import numpy as np
from scipy.special import comb
import matplotlib.pyplot as plot
from mpl_toolkits.mplot3d import Axes3D


def compute_bernstein_value(order, control_point_index, t):
    return comb(order, control_point_index) * t ** control_point_index * (1.0 - t) ** (order - control_point_index)


class BezierSurface:
    x_matrix = [[]]
    y_matrix = [[]]
    z_matrix = [[]]

    def Calculate(self, points):
        x_points = np.array([
            [points[0][0], points[1][0], points[2][0], points[3][0]],
            [points[4][0], points[5][0], points[6][0], points[7][0]]
        ])
        y_points = np.array([
            [points[0][1], points[1][1], points[2][1], points[3][1]],
            [points[4][1], points[5][1], points[6][1], points[7][1]]
        ])
        z_points = np.array([
            [points[0][2], points[1][2], points[2][2], points[3][2]],
            [points[4][2], points[5][2], points[6][2], points[7][2]]
        ])

        u_dim = 2
        v_dim = 4

        # Parameter space distribution
        u_vector = np.linspace(0, 1.0, num = 100)
        v_vector = np.linspace(0, 1.0, num = 100)

        # Bernstein matrices
        u_bernstein = np.zeros(shape = (u_dim, u_vector.size))
        v_bernstein = np.zeros(shape = (v_dim, v_vector.size))

        for u_i in range(0, u_dim):
            for u_index, u in enumerate(u_vector):
                u_bernstein[u_i][u_index] = compute_bernstein_value(u_dim - 1, u_i, u)

        for v_i in range(0, v_dim):
            for v_index, v in enumerate(v_vector):
                v_bernstein[v_i][v_index] = compute_bernstein_value(v_dim - 1, v_i, v)

        # Real-space distribution
        self.x_matrix = u_bernstein.transpose() @ x_points @ v_bernstein
        self.y_matrix = u_bernstein.transpose() @ y_points @ v_bernstein
        self.z_matrix = u_bernstein.transpose() @ z_points @ v_bernstein


if __name__ == "__main__":
    points = [
        [1.0, 1.0, 1.0],
        [2.0, 5.0, 0.0],
        [3.0, 2.0, 2.0],
        [4.0, 4.0, 1.0],
        [5.0, 1.0, 2.0],
        [6.0, 1.0, 0.0],
        [7.0, 1.0, 3.0],
        [9.0, 1.0, 3.0]
    ]

    surface = BezierSurface()
    surface.Calculate(points = points)

    fig = plot.figure(1)
    ax = fig.add_subplot(111, projection="3d")
    plot.plot([p[0] for p in points], [p[1] for p in points], [p[2] for p in points], 'o')
    ax.plot_wireframe(surface.x_matrix, surface.y_matrix, surface.z_matrix)
    plot.title("Поверхня Безьє")
    plot.show()