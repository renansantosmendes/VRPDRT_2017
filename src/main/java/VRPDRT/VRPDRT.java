/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VRPDRT;

import Algorithms.*;
import static Algorithms.Algorithms.*;
import java.util.*;
import ProblemRepresentation.*;
import GoogleMapsApi.*;
import com.google.maps.errors.ApiException;
import java.io.IOException;
import Algorithms.*;
import Controller.Controller;
import View.MainScreen;
import static Algorithms.EvolutionaryAlgorithms.*;
import static Algorithms.Methods.readProblemData;
import static Algorithms.Methods.readProblemUsingExcelData;
import InstanceReader.*;
import ReductionTechniques.HierarchicalCluster;
import jxl.read.biff.BiffException;

/**
 *
 * @author renansantos
 */
public class VRPDRT {

    final static Long timeWindows = (long) 3;
    static List<Request> requests = new ArrayList<>();
    static List<List<Integer>> listOfAdjacencies = new LinkedList<>();
    static List<List<Long>> distanceBetweenNodes = new LinkedList<>();
    static List<List<Long>> timeBetweenNodes = new LinkedList<>();
    static Set<Integer> Pmais = new HashSet<>();
    static Set<Integer> Pmenos = new HashSet<>();
    static Set<Integer> setOfNodes = new HashSet<>();
    static int numberOfNodes;
    static Map<Integer, List<Request>> requestsWhichBoardsInNode = new HashMap<>();
    static Map<Integer, List<Request>> requestsWhichLeavesInNode = new HashMap<>();
    static List<Integer> loadIndexList = new LinkedList<>();
    static Set<Integer> setOfVehicles = new HashSet<>();
    static List<Request> listOfNonAttendedRequests = new ArrayList<>();
    static List<Request> requestList = new ArrayList<>();

    //-------------------Test--------------------------------
    static Long currentTime;
    static Integer lastNode;

    public static void main(String[] args) throws ApiException, InterruptedException, IOException, BiffException {
        String directionsApiKey = "AIzaSyD9W0em7H723uVOMD6QFe_1Mns71XAi5JU";
        //String filePath = "/home/renansantos/Área de Trabalho/Excel Instances/";
        String filePath = "/home/rmendes/VRPDRT/";

        int numberOfRequests = 50;
        int requestTimeWindows = 10;
        final Integer vehicleCapacity = 4;
        String instanceSize = "s";

        int numberOfNodes = 12;
        String nodesData = "bh_n" + numberOfNodes + instanceSize;
        String adjacenciesData = "bh_adj_n" + numberOfNodes + instanceSize;
        String instanceName = buildInstaceName(nodesData, adjacenciesData, numberOfRequests, numberOfNodes,
                requestTimeWindows, instanceSize);
        final Integer numberOfVehicles = 50;

        Integer populationSize = 100;
        Integer maximumNumberOfGenerations = 10;
        Integer maximumNumberOfExecutions = 3;
        double probabilityOfMutation = 0.02;
        double probabilityOfCrossover = 0.7;
        int fileSize = populationSize;
        List<Double> parameters = new ArrayList<>();//0.0273, 0.5208, 0.0161, 0.3619, 0.0739
        List<Double> nadirPoint = new ArrayList<>();

//        new DataUpdaterUsingGoogleMapsApi(directionsApiKey, new NodeDAO(nodesData).getListOfNodes(),
//                adjacenciesData).updateAdjacenciesData();
//        
        new ScriptGenerator(instanceName, instanceSize, vehicleCapacity).generate("3d", "small");

        numberOfNodes = readProblemData(instanceName, nodesData, adjacenciesData, requests, distanceBetweenNodes,
                timeBetweenNodes, Pmais, Pmenos, requestsWhichBoardsInNode, requestsWhichLeavesInNode, setOfNodes,
                numberOfNodes, loadIndexList);
//        numberOfNodes = readProblemUsingExcelData(filePath, instanceName, nodesData, adjacenciesData, requests, distanceBetweenNodes,
//                timeBetweenNodes, Pmais, Pmenos, requestsWhichBoardsInNode, requestsWhichLeavesInNode, setOfNodes,
//                numberOfNodes, loadIndexList);

        Algorithms.printProblemInformations(requests, numberOfVehicles, vehicleCapacity, instanceName, adjacenciesData, nodesData);
        Methods.initializeFleetOfVehicles(setOfVehicles, numberOfVehicles);

        parameters.add(1.0);//1
        parameters.add((double) requestTimeWindows);//delta_t
        parameters.add((double) numberOfNodes);//n
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);// r n delta_t
        parameters.add((double) numberOfRequests * numberOfNodes);//r n
        parameters.add((double) numberOfRequests);//r
        parameters.add((double) numberOfNodes);//n
        parameters.add(1.0);//1
        parameters.add((double) numberOfRequests * numberOfNodes * requestTimeWindows);//

//        //PCA 1
//        parameters.add(0.4856772);
//        parameters.add(0.4871690);
//        parameters.add(0.4256377);
//        parameters.add(0.3744812);
//        parameters.add(0.4531891);
//        //PCA 2
//        parameters.add(-0.2753961);
//        parameters.add(0.1072986);
//        parameters.add(-0.1810329);
//        parameters.add(0.3744812);
//        parameters.add(-0.3644025);
//        
//        //PCA 1 - test
//        parameters.add(0.0025);
//        parameters.add(0.0);
//        parameters.add(0.25);
//        parameters.add(0.0);
//        parameters.add(0.25);
//        //PCA 2 - test
//        parameters.add(0.0);
//        parameters.add(0.35);
//        parameters.add(0.05);
//        parameters.add(0.80);
//        parameters.add(0.0);
        nadirPoint.add(10000000.0);
        nadirPoint.add(10000000.0);
        System.out.println("Nadir Point = " + nadirPoint);
        System.out.println("Instance Name = " + instanceName);

        NSGAII(instanceName, parameters, nadirPoint, populationSize, maximumNumberOfGenerations, maximumNumberOfExecutions, probabilityOfMutation, probabilityOfCrossover,
                requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode, numberOfNodes, vehicleCapacity, setOfVehicles,
                listOfNonAttendedRequests, requestList, loadIndexList, timeBetweenNodes, distanceBetweenNodes,
                timeWindows, currentTime, lastNode);

//        SPEA2(instanceName, parameters, nadirPoint, populationSize, fileSize, maximumNumberOfGenerations, maximumNumberOfExecutions,
//                probabilityOfMutation, probabilityOfCrossover, requests, requestsWhichBoardsInNode, requestsWhichLeavesInNode,
//                numberOfNodes, vehicleCapacity, setOfVehicles, listOfNonAttendedRequests, requestList, loadIndexList,
//                timeBetweenNodes, distanceBetweenNodes, timeWindows, currentTime, lastNode);
        //new GoogleStaticMap(new NodeDAO(nodesData).getListOfNodes(), adjacenciesData, nodesData).getStaticMapForInstance();
//        new SolutionGeneratorForAggregationTree().generateSolutionsForAggregationTree(parameters);
    }

}
