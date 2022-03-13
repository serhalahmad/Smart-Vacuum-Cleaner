# Smart-Vacuum-Cleaner
![Main Screen](https://user-images.githubusercontent.com/97162452/158079848-8f03954b-88df-46ce-a4d8-e40a6b8a1af6.jpg)

# Description
In this project a vacuum cleaning agent is designed in order to clean a room that contains dirt as well as obstacles. The agent has multiple operation modes of operation.

# Algorithms Used
* Breadth First Search (BFS)
* Depth First Search (DFS)
* Least Cost Branch and Bound (LCBB)
* Nearest Neighbor (NN)
* Genetic Algorithm
* Minimax
* Minimax with Alpha–beta pruning 
* Expectimax

# Tools Used
* Java
* JavaFX

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
![Genetic](https://user-images.githubusercontent.com/97162452/158080195-96bb8a7a-3918-42f2-aa49-dcb5469e6ca8.jpg)
This method utilizes the genetic algorithm with permutation encoding such that the chromosomes are the order of the dirty tile to be visited. The user, in this method, is given the ability to choose the number of iterations, the number of chromosomes, the mutation ratio, and the crossover ratio which can affect the results achieved. 

## Partially Observable
![Partially_obs](https://user-images.githubusercontent.com/97162452/158079844-b0d49f99-b943-433d-a2d7-1b62e7ed7528.jpg)
The environment in this mode of operation is partially visible to the agent, in other words the agent can only see whether the tile under it is dirty. The agent also doesn't know the room architecture. Therefore, when the agent is first turned on, it covers all the tiles in order to generate an image of the room architecture in its memory and during this initial sweep, it also saves the location of the dirty tiles in its memory. The method used here is DFS. In the following sweeps the agent covers the room more easily with an emphasis on the previously dirty tiles. This mode of operation resembles the actual vacuum cleaner's operation.

## Adversarial 
![Adv](https://user-images.githubusercontent.com/97162452/158079846-1a0851bd-db34-4250-8ef0-cff161e13799.jpg)
This mode enables the user to add multiple agents being either good (vacuum cleaners) or bad (dirt producers) and then the agents compete one trying to clean the room and the other trying to clean it. The methods available for the agents are minimax, alpha beta, amd expectimax with the user given the ability to choose the depth of the methods (future steps to be computed). The user can also add as many agents thoough some delay may be prooduced due to high computations being done.
