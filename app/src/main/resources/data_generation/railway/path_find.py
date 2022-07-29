# note, TRENTO, TREVISO and UDINE actually are unreacheable, it seems
# that something is wrong when decoding the JSON, need to do some debug
import json
import math
from operator import indexOf
from queue import PriorityQueue
import sys
from turtle import distance

FILE_PATH = "route_data/routes_distances_diff.json"
STATION_IDS_PATH = "route_data/statID.json"

class Graph:
    def __init__(self, num_of_vertices):
        self.v = num_of_vertices
        #adjacency matrix
        self.edges = [[-1 for i in range(num_of_vertices)] for j in range(num_of_vertices)]
        self.visited = []
    
    def add_edge(self, u, v, weight):
        self.edges[u][v] = weight

def dijikstra(graph : Graph, start_vertex):
    D = {v:float('inf') for v in range(graph.v)}
    D[start_vertex] = 0

    pq = PriorityQueue()
    pq.put((0, start_vertex))

    while not pq.empty():
        (dist, current_vertex) = pq.get()
        graph.visited.append(current_vertex)

        for neighbor in range(graph.v):
            if graph.edges[current_vertex][neighbor] != -1:
                distance = graph.edges[current_vertex][neighbor]
                if neighbor not in graph.visited:
                    old_cost = D[neighbor]
                    new_cost = D[current_vertex] + distance
                    if new_cost < old_cost:
                        pq.put((new_cost, neighbor))
                        D[neighbor] = new_cost
                        parent[neighbor] = current_vertex
    return D

def initialize_graph(data : list, ascId : list, graph : Graph, weight_key : str):
    for stations in data:
        for src in stations:
            for solution in stations[src]:
                for dst in (dict)(solution).keys():
                    distance = solution[dst][weight_key]
                    try:
                        u = ascId.index(src)
                        v = ascId.index(dst)
                        graph.add_edge(u, v, distance)
                    except ValueError as info:
                        print(f"Errore con {dst} -> {src}: {info}")


def cache_stations_ids() -> dict:
    fa = open(STATION_IDS_PATH, 'r')
    dct : dict = {}
    for stat in json.load(fa):
        for elem in stat:
            dct[elem] = stat[elem]
    return dct

def ascStations(stations):
    res = []
    for station in stations:
        res.append(station)
    return res
    

def compute_route(src : str):
    f = open(FILE_PATH, 'r')
    stations = json.load(f)
    graph = Graph(len(stIds))
    initialize_graph(stations, stIds, graph, "distance")
    return dijikstra(graph, stIds.index(src))
    
def printPath(j):
    if parent[j] == -1:
        print(f'-{stIds[j]}')
        return
    printPath(parent[j])
    print(f'-{stIds[j]}')

ids = cache_stations_ids()
stIds = ascStations(ids.keys())
parent = [-1] * len(stIds)


if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python pathFind.py <srcStationName> <solutionStationName> \
            \nnote: all station names has to be in caps lock and wrapped in single/double quotes if \
            \nthe name contains a space. All stations' names are in route_data/statID.json")
        sys.exit(0)

    src = sys.argv[1]
    solution = sys.argv[2]
    path = compute_route(src)
    print(f"La distanza da {src} a {solution} è di {math.ceil(path[stIds.index(solution)])} km")
    print("il percorso da eseguire è il seguente: ")
    printPath(stIds.index(solution))
    
