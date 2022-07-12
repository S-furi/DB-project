import json
import math
from queue import PriorityQueue
import sys

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

def initialize_graph(data : dict, ascId : list, graph : Graph, weight_key : str):
    for key in data.keys():
        for value in data[key]:
            try:
                u = ascId.index(key)
                v = ascId.index(value)
                graph.add_edge(u, v, data[key][value][weight_key])
            except ValueError:
                pass

def cache_stations_ids() -> dict:
    fa = open(STATION_IDS_PATH, 'r')
    return dict(json.load(fa))

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
        print("Usage: python pathFind.py <srcStationName> <dstStationName> \
            \nnote: all station names has to be in caps lock and wrapped in single/double quotes if \
            \nthe name contains a space. All stations' names are in route_data/statID.json")
        sys.exit(0)

    src = sys.argv[1]
    dst = sys.argv[2]
    path = compute_route(src)
    print(f"La distanza da {src} a {dst} è di {math.ceil(path[stIds.index(dst)])} km")
    print("il percorso da eseguire è il seguente: ")
    printPath(stIds.index(dst))
    
