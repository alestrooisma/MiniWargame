package aetherdriven;

import com.badlogic.gdx.math.Vector2;

public class Maths {
    public static final Vector2 intersection1 = new Vector2();
    public static final Vector2 intersection2 = new Vector2();

    /**
     * Return the distance between line a*x + b*y = c and point (x0, xy).
     *
     * @param a  x-coefficient describing the line
     * @param b  y-coefficient describing the line
     * @param c  constant coefficient describing the line
     * @param x0 x-coordinate of the point
     * @param y0 y-coordinate of point
     * @return the distance between the line and the point
     */
    public static float distance(float a, float b, float c, float x0, float y0) {
        return Math.abs(a * x0 + b * y0 - c) / sqrt(a * a + b * b);
    }

    /**
     * Return the squared distance between line a*x + b*y = c and point (x0, xy).
     *
     * This can be used instead of the regular distance function to save on a square root operation.
     *
     * @param a  x-coefficient describing the line
     * @param b  y-coefficient describing the line
     * @param c  constant coefficient describing the line
     * @param x0 x-coordinate of the point
     * @param y0 y-coordinate of point
     * @return the distance between the line and the point
     */
    public static float distanceSquared(float a, float b, float c, float x0, float y0) {
        float f = a * x0 + b * y0 - c;
        return (f * f) / (a * a + b * b);
    }

    /**
     * Calculate the intersections between line a*x + b*y = c and a circle with radius r
     * centered around (x0, xy).
     * <p>
     * The number of intersections is returned and the intersections are set to the output arguments
     * p1 and p2, only if the number of intersections is greater than 0.
     *
     * @param a  x-coefficient describing the line
     * @param b  y-coefficient describing the line
     * @param c  constant coefficient describing the line
     * @param x0 x-coordinate of the circle's center
     * @param y0 y-coordinate of the circle's center
     * @param r  radius of the circle
     * @param p1 first intersection (only valid if return value > 0)
     * @param p2 second intersection (only valid if return value > 0)
     * @return the number of intersections
     */
    public static int intersection(float a, float b, float c, float x0, float y0, float r, Vector2 p1, Vector2 p2) {
        // Prepare a few values
        float c0 = c - a * x0 - b * y0;
        float a2b2 = a * a + b * b;

        // Calculate discriminant
        float D = r * r * a2b2 - c0 * c0;

        // Determine number of intersections
        int number_of_intersections;
        if (D > 0) {
            number_of_intersections = 2;
        } else if (D == 0) {
            number_of_intersections = 1;
        } else {
            return 0; // Do NOT calculate intersection if there are none!
        }

        // Calculate intersections
        float sqrtD = sqrt(D);
        p1.x = (a * c0 + b * sqrtD) / a2b2 + x0;
        p1.y = (b * c0 - a * sqrtD) / a2b2 + y0;
        p2.x = (a * c0 - b * sqrtD) / a2b2 + x0;
        p2.y = (b * c0 + a * sqrtD) / a2b2 + y0;

        return number_of_intersections;
    }

    /**
     * Calculate the intersections between line a*x + b*y = c and a circle with radius r
     * centered around (x0, xy), and set the result to static variables.
     * <p>
     * The number of intersections is returned and the intersections are set to the output arguments
     * p1 and p2, only if the number of intersections is greater than 0.
     *
     * @param a  x-coefficient describing the line
     * @param b  y-coefficient describing the line
     * @param c  constant coefficient describing the line
     * @param x0 x-coordinate of the circle's center
     * @param y0 y-coordinate of the circle's center
     * @param r  radius of the circle
     * @return the number of intersections
     */
    public static int intersection(float a, float b, float c, float x0, float y0, float r) {
        return intersection(a, b, c, x0, y0, r, intersection1, intersection2);
    }

    public static float sqrt(float val) {
        return (float) Math.sqrt(val);
    }
}
