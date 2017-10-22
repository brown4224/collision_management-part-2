package com.jeff;

//import org.omg.CORBA.Object;

import java.util.concurrent.Callable;

public class CollisionManagement implements Callable<Integer>{
    private Object[][] state;
    private int halt_plane;
    private int look_ahead;

    public CollisionManagement(Object[][] state, int halt_plane,  int look_ahead) {
        this.state = clone_state(state);
        this.halt_plane = halt_plane;
        this.look_ahead = look_ahead;
    }

    @Override
    public Integer call(){
        int counter = 0;

        try{
            for (int i = 0; i< look_ahead; i++){

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

    private Object[][] clone_state(Object[][] state){
        Object[][] clone = new Object [state.length][];
        for (int i = 0; i < state.length; i++ )
            clone[i] = state[i].clone();
        return clone;
    }
}
