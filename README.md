# Smart-Vacuum-Cleaner
![Main Screen](https://user-images.githubusercontent.com/97162452/158079848-8f03954b-88df-46ce-a4d8-e40a6b8a1af6.jpg)

# Description
In this project a vacuum cleaning agent is designed in order to clean a room that contains dirt as well as obstacles. The agent has multiple operation modes of operation.

# Modes of Operation

## Fully Observable
![Fully_obs](https://user-images.githubusercontent.com/97162452/158079847-130e3b30-27fb-44f3-b6c0-4349ae10d92d.jpg)

The environment is fully visible to the agent. So the agent computes the shortest path to all the dirt tiles, similar to the travelling salesman problem, and then proceeds to clean them.

### Methods

#### Brute Force
This method is able to find the shortest path possible. However, that comes at the expense of the computation time. Brute force has a time complexity of `n!`, where n is the number of dirty tiles. Therefore, performing brute force on more than 10 dirt tiles will take too much time.

#### Least Cost Branch and Bound
This method is less computationally deamnding than the brute force method. However, this leads to sub-optimal paths. The time complexity of this method is ``n^{2}*2^{n}``, which is much less than brute force, but still relatively high such that the agent can handle around 20 dirt tiles before starting to show an increased delay in processing.

#### Nearest Neighbor
This method is the least computationally demanding since the agent directly goes to the nearest dirty tile without computing further steps. This method despite being the fastest produces bad results where the path chosen is somtimes very inefficient. 

#### Genetic Algorithm 
This method utilizes the 

## Partially Observable
![Partially_obs](https://user-images.githubusercontent.com/97162452/158079844-b0d49f99-b943-433d-a2d7-1b62e7ed7528.jpg)


## Adversarial 
![Adv](https://user-images.githubusercontent.com/97162452/158079846-1a0851bd-db34-4250-8ef0-cff161e13799.jpg)


## Hardware Connections
<img src="Hardware Circuitry .png" alt="Hardware Circuit" title="Hardware Circuit">


## ROS RQT-Graph
<img src="RQT.jpeg" alt="RQT-Graph" title="RQT-Graph">

The `video_pub_py` node is running on the PC and computates the action that the user wants to execute and sends this command to `n__motor_run_py` which is running on the rover.
The `n__ultrasonic_py` node is running on the Rover and publishes to the motor node and the buzzer node.
The `n__rover_pub_py` node sends the video frames from the Rover to the PC `rover_frames`.
