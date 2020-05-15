import numpy as np
from scipy.special import comb
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D


def compute_bernstein_value(order, control_point_index, t):
    return comb(order, control_point_index) * t ** control_point_index * (1.0 - t) ** (order - control_point_index)


if __name__ == "__main__":
    ###########################################################################
    # Define curve's control points
    ###########################################################################
    x_control_point_matrix = np.array([
        [1.0, 2.0, 3.0, 4.0],
        [5.0, 6.0, 7.0, 9.0]
    ])

    y_control_point_matrix = np.array([
        [1.0, 5.0, 2.0, 4.0],
        [1.0, 1.0, 1.0, 1.0]
    ])

    z_control_point_matrix = np.array([
        [1.0, 0.0, 2.0, 1.0],
        [2.0, 0.0, 3.0, 3.0]
    ])

    ###########################################################################
    # Define order
    ###########################################################################
    assert(x_control_point_matrix.shape == y_control_point_matrix.shape == z_control_point_matrix.shape)
    control_point_u_dimension = x_control_point_matrix.shape[0]
    control_point_v_dimension = y_control_point_matrix.shape[1]
    u_order = control_point_u_dimension - 1
    v_order = control_point_v_dimension - 1

    ###########################################################################
    # Define surface's linear parameter-space distribution
    ###########################################################################
    u_vector = np.linspace(0, 1.0, num=100)
    v_vector = np.linspace(0, 1.0, num=100)

    ###########################################################################
    # Compute surface's Bernstein matrices
    ###########################################################################
    u_bernstein_matrix = np.zeros(shape=(control_point_u_dimension, u_vector.size))
    v_bernstein_matrix = np.zeros(shape=(control_point_v_dimension, v_vector.size))

    for u_control_point_index in range(0, control_point_u_dimension):
        for u_index, u in enumerate(u_vector):
            u_bernstein_matrix[u_control_point_index][u_index] = compute_bernstein_value(u_order, u_control_point_index, u)

    for v_control_point_index in range(0, control_point_v_dimension):
        for v_index, v in enumerate(v_vector):
            v_bernstein_matrix[v_control_point_index][v_index] = compute_bernstein_value(v_order, v_control_point_index, v)

    ###########################################################################
    # Compute surface's real-space distribution
    ###########################################################################
    x_matrix = u_bernstein_matrix.transpose() @ x_control_point_matrix @ v_bernstein_matrix
    y_matrix = u_bernstein_matrix.transpose() @ y_control_point_matrix @ v_bernstein_matrix
    z_matrix = u_bernstein_matrix.transpose() @ z_control_point_matrix @ v_bernstein_matrix

    ###########################################################################
    # Plot surface
    ###########################################################################
    fig = plt.figure(1)

    ax = fig.add_subplot(111, projection="3d")
    ax.plot_wireframe(x_matrix, y_matrix, z_matrix)
    plt.title("Torus Patch Defined From Control Points")
    plt.show()