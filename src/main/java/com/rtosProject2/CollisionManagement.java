package com.rtosProject2;


import java.util.concurrent.Callable;

public class CollisionManagement implements Callable<Integer>{
    private Object[][] state;
    private int halt_plane;
    private int look_ahead;

    public CollisionManagement(Positions positions, int halt_plane,  int look_ahead) {

        Object[][] state = new Object[positions.size()][3];
        for (int pos = 0; pos < positions.size(); pos++) {
            Position position = positions.get(pos);
            state[pos][0] = position.train();
            state[pos][1] = position.row();
            state[pos][2] = position.col();
        }

        this.state = state;
        this.halt_plane = halt_plane;
        this.look_ahead = look_ahead;
    }

    @Override
    public Integer call(){
        int counter = 0;

        try{
            for (int i = 0; i < look_ahead; i++){

                // IF collision:  We are done looking ahead
                if(     (state[0][1] == state[1][1] && state[0][2] == state[1][2]) ||
                        (state[0][1] == state[2][1] && state[0][2] == state[2][2]) ||
                        (state[1][1] == state[2][1] && state[1][2] == state[2][2])
                        )
                    break;

                // Move train if not halted
                if(halt_plane != 0) {
                    state[0][1] = ((int) state[0][1] + 1) % Plane.rows;
                    state[0][2] = ((int) state[0][2] + 1) % Plane.cols;
                }
                if(halt_plane != 1) {
                    state[1][1] = ((int) state[1][1] + 1) % Plane.rows;
                }
                if(halt_plane != 2) {
                    state[2][2] = ((int) state[2][2] + 1) % Plane.cols;
                }
                counter++;

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return counter;
    }
}
