package com.iea.gui;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Agent extends Rectangle {
    private Point position;
    int cost, combinations;
    long time;
    long t;
    String mode;
    int deltaX;
    int deltaY;
    int[][] adjacencyMatrix;
    int[] path;
    Path[][] pathBetweenNode;
    public Tile[][] observedRoom;
    boolean[][] visited;
    Stack<Point> stack;
    ArrayList<Point> memory;
    ArrayList<Point> roomTiles;
    public int status;
    int delay = 300;
    String type;
    int epoch = 10000;
    int population = 5000;
    double crossOverRate = 0.2;
    double mutationRate = 0.7;

    public Agent(Point position, int x, int y, int w, int h, String type) {
        super(w, h);
        this.type = type;
        this.position = new Point(position);
        setTranslateX(x);
        setTranslateY(y);
        cost = Integer.MAX_VALUE;
        combinations = 0;
        time = 0;
        deltaX = w + 5;
        deltaY = h + 5;
        memory = new ArrayList<>();
        roomTiles = new ArrayList<>();
    }

    public Agent(Agent agent, int x, int y, int w) {
        super(w, w);
        this.type = agent.type;
        this.position = new Point(agent.position);
        setTranslateX(x);
        setTranslateY(y);
        cost = agent.cost;
        combinations = agent.combinations;
        time = agent.time;
        deltaX = w + 5;
        deltaY = w + 5;
        memory = new ArrayList<>(agent.memory);
        roomTiles = new ArrayList<>(agent.roomTiles);
    }

    public Agent(Agent agent) {
        this.position = new Point(agent.getPosition());
        this.type = agent.type;
        this.status = agent.status;
    }

    void moveLeft() {
        setTranslateX(getTranslateX() - deltaX);
        position.translate(-1, 0);
    }

    void moveRight() {
        setTranslateX(getTranslateX() + deltaX);
        position.translate(1, 0);
    }

    void moveUp() {
        setTranslateY(getTranslateY() - deltaY);
        position.translate(0, -1);
    }

    void moveDown() {
        setTranslateY(getTranslateY() + deltaY);
        position.translate(0, 1);
    }

    int[][] generateAdjacencyMatrix(List<Point> cities, Tile[][] tiles, boolean messageDisplayed) {
        int distance;
        int[][] tileWeights = new int[Main.game.getHeight()][Main.game.getWidth()];
        int dirt = cities.size();
        cities.add(new Point(getPosition()));
        int[][] adjMatrix = new int[dirt + 1][dirt + 1];
        pathBetweenNode = new Path[dirt + 1][dirt + 1];
        Arrays.stream(adjMatrix).forEach(a -> Arrays.fill(a, -1));
        Queue<Point> frontier = new ArrayDeque<>();
        for (int i = dirt; i > 0; i--) {
            frontier.clear();
            frontier.add(cities.get(i));
            Arrays.stream(tileWeights).forEach(a -> Arrays.fill(a, -1));
            tileWeights[frontier.peek().y][frontier.peek().x] = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (tileWeights[cities.get(j).y][cities.get(j).x] != -1) {
                    distance = tileWeights[cities.get(j).y][cities.get(j).x];
                } else {
                    distance = getDistanceBFS(frontier, cities.get(j), tileWeights, tiles);
                }
                if (distance == -1) {
                    cities.remove(j);
                    if (!messageDisplayed) {
//                        GUI.displayMessage("ERROR", "Can not reach dirty tile!");
                    }
                    return generateAdjacencyMatrix(cities, tiles, true);
                } else {
                    Path p = new Path(backtrack(cities.get(i), cities.get(j), tileWeights));
                    p.setCost(distance);
                    pathBetweenNode[i][j] = new Path(p);
                    pathBetweenNode[j][i] = new Path(p.reverse());
                    adjMatrix[i][j] = adjMatrix[j][i] = distance;
                }
            }
        }
        adjacencyMatrix = adjMatrix;
        return adjMatrix;
    }


    int getDistance(Room room, Point destination) {
        int[][] tileWeights = new int[Main.game.getHeight()][Main.game.getWidth()];
        Arrays.stream(tileWeights).forEach(a -> Arrays.fill(a, -1));
        Queue<Point> frontier = new ArrayDeque<>();
        frontier.add(new Point(getPosition()));
        tileWeights[getPosition().y][getPosition().x] = 0;
        while (!frontier.isEmpty()) {
            Point source = frontier.remove();
            Point current = new Point(source);
            ArrayList<Point> neighbors = current.getNeighbors(room, destination);
            for (Point p : neighbors) {
                if (tileWeights[p.y][p.x] == -1) {
                    tileWeights[p.y][p.x] = tileWeights[source.y][source.x] + 1;
                    frontier.add(new Point(p));
                }
            }
            if (source.equals(destination)) {
                return tileWeights[destination.y][destination.x];
            }
        }
        return Integer.MAX_VALUE;
    }

    public int start(Room room, boolean isMaximizer) {
        return -1;
    }

    static int getDistanceBFS(Queue<Point> frontier, Point destination, int[][] tileWeights, Tile[][] tiles) {
        while (!frontier.isEmpty()) {
            Point source = frontier.remove();
            Point current = new Point(source);
            ArrayList<Point> neighbors = current.getNeighbors();
            for (Point p : neighbors) {
                if ((tiles[p.y][p.x].isValid()) && tileWeights[p.y][p.x] == -1) {
                    tileWeights[p.y][p.x] = tileWeights[source.y][source.x] + 1;
                    frontier.add(new Point(p));
                }
            }
            if (source.equals(destination)) {
                return tileWeights[destination.y][destination.x];
            }
        }
        return -1;
    }

    static Path backtrack(Point source, Point destination, int[][] weights) {
        Path path = new Path();
        Point current = new Point(destination);
        path.addNode(destination);
        cont:
        while (!current.equals(source)) {
            ArrayList<Point> neighbors = current.getNeighbors(true);
            for (Point p : neighbors) {
                if (weights[p.y][p.x] == weights[destination.y][destination.x] - 1) {
                    path.addNodeFirst(new Point(p));
                    destination = new Point(p);
                    current = new Point(p);
                    continue cont;
                }
            }
            current = new Point(path.path().remove(0));
        }
        return path;
    }

    public void partiallyObservable() {
        mode="prev";
        stack = new Stack<>();
        observedRoom = new Tile[Main.game.getHeight()][Main.game.getWidth()];
        visited = new boolean[Main.game.getHeight()][Main.game.getWidth()];
        stack.push(position);
        visited[position.y][position.x] = true;
        observedRoom[position.y][position.x] = new Tile(position.x, position.y);
        observedRoom[position.y][position.x].setStatus(3);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getRoom();
                if (stack.isEmpty()) {
                    timer.purge();
                    timer.cancel();
                        t = System.currentTimeMillis();
                    try {
                        phase2();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, delay);
    }

    private void phase2() throws InterruptedException {
        Timer timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - t > 1000) {
                    if(mode.equals("prev")){
                        mode="sweep";
                    System.out.println("Cleaning Previous");
                    cleanPrevs();}
                    else if(mode.equals("sweep")){
                        mode="prev";
                        System.out.println("Cleaning All");
                        sweepRoom();
                    }
                }
            }
        }, 0, delay);
    }

    private void cleanPrevs() {
        generateAdjacencyMatrix(memory, observedRoom, false);
        nearestNeighbor();
        Path path = Main.game.getPath(Main.game.agent);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    t=System.currentTimeMillis();
                    newDirtyTiles();
                    Point p = path.path().remove(0);
                    Main.game.goToNext(Main.game.agent, p);
                } catch (Exception e) {
                    t=System.currentTimeMillis();
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 0, delay);
    }

    public void newDirtyTiles() {
        ArrayList<Point> neighbors = position.getNeighbors();
        for (Point n : neighbors) {
            if (Main.game.tiles[n.y][n.x].isDirty() && !memory.contains(n)) {
                memory.add(n);
                observedRoom[n.y][n.x].incrementCount();
            }
        }
    }

    private void sweepRoom() {
        generateAdjacencyMatrix(roomTiles, observedRoom, false);
        nearestNeighbor();
        Path path = Main.game.getPath(Main.game.agent);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    t=System.currentTimeMillis();
                    newDirtyTiles();
                    Point p = path.path().remove(0);
                    Main.game.goToNext(Main.game.agent, p);
                } catch (Exception e) {
                    t=System.currentTimeMillis();
                    timer.purge();
                    timer.cancel();
                }
            }
        }, 0, delay);
    }

    private void getRoom() {
        ArrayList<Point> neighbors = position.getNeighbors();
        roomTiles.add(new Point(position));
        observedRoom[position.y][position.x] = new Tile(position.x, position.y);
        observedRoom[position.y][position.x].hasWallRight = Main.game.tiles[position.y][position.x].hasWallRight();
        observedRoom[position.y][position.x].hasWallLeft = Main.game.tiles[position.y][position.x].hasWallLeft();
        observedRoom[position.y][position.x].hasWallUp = Main.game.tiles[position.y][position.x].hasWallUp();
        observedRoom[position.y][position.x].hasWallDown = Main.game.tiles[position.y][position.x].hasWallDown();
        for (Point n : neighbors) {
            if (!visited[n.y][n.x]) {
                if (!roomTiles.contains(n)) {
                    roomTiles.add(new Point(n));
                }
                observedRoom[n.y][n.x] = new Tile(n.x, n.y);
                if (Main.game.tiles[n.y][n.x].isClean()) {
                    observedRoom[n.y][n.x].setStatus(0);
                } else {
                    visited[n.y][n.x] = true;
                    observedRoom[n.y][n.x].incrementCount();
                    if (!memory.contains(new Point(n)))
                        memory.add(new Point(n));
                    stack.push(new Point(n));
                    Main.game.goToNext(Main.game.agent, n);
                    return;
                }
            }
        }
        for (Point n : neighbors) {
            if (!visited[n.y][n.x]) {
                visited[n.y][n.x] = true;
                stack.push(new Point(n));
                Main.game.goToNext(Main.game.agent, n);
                return;
            }
        }
        try {
            stack.pop();
            Main.game.goToNext(Main.game.agent, stack.peek());
        } catch (Exception e) {
        }
    }

    public void bruteForce() {
        long startTime = System.currentTimeMillis();
        int size = getAdjacencyMatrix().length;
        path = new int[size];
        setCombinations(0);
        setCost(Integer.MAX_VALUE);
        boolean[] visited = new boolean[size - 1];
        getPath()[0] = size - 1;
        bruteForceRecursive(size - 1, visited, 0, 1, getPath());
        setTime(System.currentTimeMillis() - startTime);
    }

    private void bruteForceRecursive(int source, boolean[] visited, int cost, int level, int[] p) {
        if (level == getPath().length && cost < getCost()) {
            setCombinations(getCombinations() + 1);
            setCost(cost);
            path = p.clone();
            return;
        }
        for (int i = 0; i < p.length - 1; i++) {
            if (!visited[i]) {
                visited[i] = true;
                int edgeCost = getAdjacencyMatrix()[source][i];
                p[level] = i;
                cost += edgeCost;
                if (cost < getCost()) {
                    bruteForceRecursive(i, visited, cost, level + 1, p);
                }
                visited[i] = false;
                cost -= edgeCost;
            }
        }
    }

    public void branchAndBound() {
        long startTime = System.currentTimeMillis();
        int size = getAdjacencyMatrix().length;
        int[][] minEdgesFromNode = new int[size][2];
        int[][] weight = getAdjacencyMatrix().clone();
        boolean[] visited = new boolean[size];
        path = new int[size];
        int cost = 0;
        for (int i = 0; i < size; i++) {
            minEdgesFromNode[i][0] = getMinEdge(weight, i);
            minEdgesFromNode[i][1] = getSecondMinEdge(weight, i);
            cost += minEdgesFromNode[i][0] + minEdgesFromNode[i][1];
        }
        cost = (cost == 1) ? 1 : cost / 2;
        path[0] = size - 1;
        visited[size - 1] = true;
        branchAndBoundRecursive(weight, cost, 0, 1, path, visited, minEdgesFromNode);
        setTime(System.currentTimeMillis() - startTime);
    }

    private void branchAndBoundRecursive(int[][] weight, int cost, int currentCost, int level, int[] p, boolean[] visited, int[][] minEdgesFromNode) {
        if (level == weight.length) {
            setCombinations(getCombinations() + 1);
            if (currentCost < getCost()) {
                this.path = p.clone();
                setCost(currentCost);
                return;
            }
        }
        for (int i = 0; i < weight.length; i++) {
            if (!visited[i]) {
                int temp = cost;
                currentCost += weight[p[level - 1]][i];
                if (level == 1) {
                    cost -= (minEdgesFromNode[p[level - 1]][0] + minEdgesFromNode[i][0]) / 2;
                } else {
                    cost -= (minEdgesFromNode[p[level - 1]][1] + minEdgesFromNode[i][0]) / 2;
                }
                if (cost + currentCost < getCost()) {
                    p[level] = i;
                    visited[i] = true;
                    branchAndBoundRecursive(weight, cost, currentCost, level + 1, p, visited, minEdgesFromNode);
                }
                currentCost -= weight[p[level - 1]][i];
                cost = temp;
                visited[i] = false;
            }
        }
    }

    public void nearestNeighbor() {
        long startTime = System.currentTimeMillis();
        int size = getAdjacencyMatrix().length;
        setCost(0);
        path = new int[size];
        boolean[] visited = new boolean[size - 1];
        int level = 1;
        int indexOfMinimum = -1;
        getPath()[level - 1] = size - 1;
        while (level < size) {
            int minimum = Integer.MAX_VALUE;
            for (int i = 0; i < size - 1; i++) {
                int temp = getAdjacencyMatrix()[getPath()[level - 1]][i];
                if (temp < minimum && temp != -1 && !visited[i]) {
                    minimum = temp;
                    indexOfMinimum = i;
                }
            }
            if (indexOfMinimum != -1) {
                visited[indexOfMinimum] = true;
                setCost(getCost() + minimum);
                level++;
                getPath()[level - 1] = indexOfMinimum;
            }
        }
        setTime(System.currentTimeMillis() - startTime);
    }

    public void genetic() {
        Random rnd = new Random();
        int size = getAdjacencyMatrix().length;
        Path[] chromosomes = new Path[population];
        Path best = new Path();
        path = new int[size];
        best.setCost(Integer.MAX_VALUE);
        for (int i = 0; i < population; i++) {
            chromosomes[i] = new Path(getRandomPath(rnd));
        }
        for (int i = 1; i <= epoch; i++) {
            chromosomes = selection(chromosomes);
            crossOver(crossOverRate, chromosomes, rnd);
            mutate(mutationRate, chromosomes, rnd);
            computeCost(chromosomes);
            best = new Path(getBestPath(best, chromosomes));
            System.out.println("Epoch: " + i + "\n\tBest Path: " + best.printGenetic() + "\tCost: " + best.cost());
        }
        this.setPath(best.getPath());
    }

    private void crossOver(double crossOverRate, Path[] chromosomes, Random rnd) {
        Arrays.sort(chromosomes, new compare());
        for (int i = 0; i < chromosomes.length - 1; i++) {
            if (Math.random() < crossOverRate) {
                int i1 = rnd.nextInt(chromosomes[i].getGeneticPath().size() - 1) + 1;
                for (int k = i1; k < chromosomes[i].getGeneticPath().size(); k++) {
                    for (int j = 0; j < chromosomes[i].getGeneticPath().size(); j++) {
                        if (!chromosomes[i + 1].getGeneticPath().subList(0, k).contains(chromosomes[i].getGeneticPath().get(j))) {
                            chromosomes[i + 1].getGeneticPath().set(k, chromosomes[i].getGeneticPath().get(j));
                            break;
                        }
                    }
                }
                for (int k = i1; k < chromosomes[i].getGeneticPath().size(); k++) {
                    for (int j = 0; j < chromosomes[i].getGeneticPath().size(); j++) {
                        if (!chromosomes[i].getGeneticPath().subList(0, k).contains(chromosomes[i + 1].getGeneticPath().get(j))) {
                            chromosomes[i].getGeneticPath().set(k, chromosomes[i + 1].getGeneticPath().get(j));
                            break;
                        }
                    }
                }
            }
        }
    }

    private Path[] selection(Path[] chromosomes) {
        Path[] toReturn = new Path[chromosomes.length];
        Arrays.sort(chromosomes, new compare());
        Hashtable<Integer, java.lang.Double> dict = new Hashtable<>(createRoulette(chromosomes));
        for (int i = 0; i < chromosomes.length; i++) {
            double random = Math.random();
            for (int j = 0; j < chromosomes.length; j++) {
                if (random < dict.get(j)) {
                    toReturn[i] = new Path(chromosomes[j]);
                    break;
                }
            }
        }
        return toReturn;
    }

    private Hashtable<Integer, java.lang.Double> createRoulette(Path[] chromosomes) {
        Hashtable<Integer, java.lang.Double> toReturn = new Hashtable<>();
        double cumm = 0;
        double totalFitness = 0;
        for (Path chromosome : chromosomes) {
            totalFitness += chromosome.getFitness();
        }
        for (int i = 0; i < chromosomes.length; i++) {
            cumm = (chromosomes[i].getFitness() / totalFitness) + cumm;
            toReturn.put(i, cumm);
        }
        return toReturn;
    }

    private void mutate(double mutationRate, Path[] paths, Random rnd) {
        for (Path path : paths) {
            if (Math.random() < mutationRate) {
                for (int i = 0; i < rnd.nextInt(path.getGeneticPath().size() - 1) + 1; i++) {
                    int i1 = rnd.nextInt(path.getGeneticPath().size() - 1) + 1;
                    int i2 = rnd.nextInt(path.getGeneticPath().size() - 1) + 1;
                    Collections.swap(path.getGeneticPath(), i1, i2);
                }
            }
        }
    }

    private Path getBestPath(Path best, Path[] paths) {
        for (Path p : paths) {
            if (p.cost() < best.cost()) {
                best = new Path(p);
            }
        }
        return best;
    }

    private void computeCost(Path[] paths) {
        int cost = 0;
        for (Path p : paths) {
            for (int i = 1; i < p.getGeneticPath().size(); i++) {
                cost += getAdjacencyMatrix()[p.getGeneticPath().get(i - 1)][p.getGeneticPath().get(i)];
                p.setCost(cost);
            }
        }
    }

    private Path getRandomPath(Random rnd) {
        int size = getAdjacencyMatrix().length;
        int cost = 0;
        Path toReturn = new Path();
        int[] path = new int[size];
        int i = 1;
        boolean[] visited = new boolean[size];
        path[0] = size - 1;
        while (i < size) {
            int node = rnd.nextInt(size - 1);
            if (!visited[node]) {
                cost += getAdjacencyMatrix()[path[i - 1]][path[i]];
                visited[node] = true;
                path[i] = node;
                i++;
            }
        }
        toReturn.setGeneticPath(path);
        toReturn.setCost(cost);
        return toReturn;
    }

    static int getMinEdge(int[][] weight, int i) {
        int minimum = Integer.MAX_VALUE;
        for (int j = 0; j < weight[0].length; j++) {
            if (weight[i][j] < minimum && weight[i][j] != -1 && i != j) {
                minimum = weight[i][j];
            }
        }
        return minimum;
    }

    static int getSecondMinEdge(int[][] weight, int i) {
        int minimum = Integer.MAX_VALUE, secondMinimum = Integer.MAX_VALUE;
        for (int j = 0; j < weight[0].length; j++) {
            if (i == j)
                continue;
            if (weight[i][j] <= minimum && weight[i][j] != -1) {
                secondMinimum = minimum;
                minimum = weight[i][j];
            } else if (weight[i][j] <= secondMinimum && weight[i][j] != minimum && weight[i][j] != -1) {
                secondMinimum = weight[i][j];
            }
        }
        return secondMinimum;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }


    public int[] getPath() {
        return path;
    }

    public void setPath(int[] path) {
        this.path = path;
    }

    public Path[][] getPathBetweenNode() {
        return pathBetweenNode;
    }


    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p) {
        position = new Point(p);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int c) {
        cost = c;
    }

    public int getCombinations() {
        return combinations;
    }

    public void setCombinations(int c) {
        combinations = c;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long t) {
        time = t;
    }

    public Point getNextMove() {
        return null;
    }

    public void setNextMove(Point p) {
    }

    public void setDepth(int value) {
    }
}

class compare implements Comparator<Path> {
    @Override
    public int compare(Path o1, Path o2) {
        return (o1.cost() - o2.cost());
    }
}

//class compareMem implements Comparator<Point> {
//    @Override
//    public int compare(Point o1, Point o2) {
//        return Agent.observedRoom[o1.y][o1.x].getCount() - Agent.observedRoom[o2.y][o2.x].getCount();
//    }
//}
