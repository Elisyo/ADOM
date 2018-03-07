# ADOM

Projet réalisé par Clémence Buche et Florian Guilbert

* Compilation *
- Pour compiler, ouvrez un terminal
- Allez dans le répertoire dans lequel se trouve le projet
- Tapez la commande "java -jar ADOM.jar"
- Un menu s'affiche:  

======================== MENU ========================
Que voulez-vous faire ?
1. SingleCritere
2. MultiCritere
3. Sortie du programme
Merci d'indiquer le numéro correspondant.


* Explication du projet *
Le TSP (problème du voyageur de commerce, ou travelling salesman problem) est un problème classique de l’optimisation combinatoire. Etant donné un ensemble de villes, il s’agit de trouver le circuit “optimal” qui relie toutes les villes, sans passer plus d’une fois par la même. L’optimalité se rapporte généralement à une distance parcourue, ou un coût, à minimiser. Le TSP est connu comme un problème NP-difficile : on ne connaît pas d’algorithme permettant de trouver une solution exacte en un temps polynomial. Néanmoins, de nombreuses applications réelles dans différents domaines (transport, ordonnancement, logistique) se rapportent à un problème de TSP, ou se doivent de résoudre le TSP comme sous-problème. Plus formellement, le TSP peut être défini par un graphe complet G = (V, E), o`u V = {v1, v2, ..., vn} est un ensemble de noeuds (les villes) et E = {[vi , vj ], vi , vj ∈ V } est un ensemble d’arcs. A chaque arc [  vi , vj ] ∈ E est affecté un coût positif c(i, j), donné par une matrice de coûts. Le but est de trouver la permutation cyclique simple π de taille n qui minimise la fonction suivante : 

f(π) = i=1n-1 c ( π(i), π(i + 1) ) + c ( π(n), π(1) ) 

(la partie droite de la formule correspond au retour à la ville de départ).


Les instances que nous allons utiliser proviennent de la TSPLIB. Il s’agit d’instances euclidiennes à 100 villes. Un fichier d’instance contient la position de chaque ville dans un espace euclidien. Nous nous intéressons aux 2 instances suivantes : kroA100.tsp, kroB100.tsp .


* Explication des algorithmes *

	I. Solution aléatoire
Tout d'abord, nous avons réalisé un algorithme permettant de générer une solution aléatoire. Nous générons une liste d’entiers entre 0 et 99 inclus pour avoir notre graphe complet. Puis nous calculons le coût de chacune des distances pour avoir le coût global du graphe. 


	II. Heuristique
Nous avons un point de départ (starter dans notre code) qui représente notre ville de départ. Ensuite, à partir de ce point de départ, que nous considérons comme un point A. Nous cherchons le point B (une ville “disponible”) pour lequel la distance est minimale.
Une ville est disponible si elle n’est pas encore utilisée dans notre graphe.
A partir de ce point B, il devient notre nouveau point A et nous recommençons l’algorithme.
Une fois arrivé à la dernière ville, le graphe n’est pas complet, il faut relier la dernière et la 1ère.


	III. Voisinage - Swap
Avec cet algorithme, il s’agit d’échanger la position de 2 villes passées en
Paramètre.


	IV. Voisinage - Two-opt
Le principe de cet algorithme est un peu plus complexe que le swap. Nous prenons en paramètre la liste des villes (qui constitue notre graphe), nous tirons aléatoirement une ville A et une ville B qui sont présentes dans la liste de villes. Toutes les villes qui sont comprises entre la ville A à la ville B vont être inversées.
Pour se faire, nous formons une sous-liste temporaire correspondant à cet intervalle que nous inversons puis nous remplaçons le segment dans la liste principale.


	V. Order Based Crossover - Evolutionnaire
Dans cet algorithme, nous créons une population de dix graphes complets (10 listes de 100 villes, que nous appelons les “ancêtres”). Ces ancêtres sont générées grâce à l’heuristique de dix villes choisies aléatoirement (à part la 1ère ville qui sera toujours présente). Nous choisissons deux listes parmi ces ancêtres; nous choisissons les deux listes ayant les coûts globaux les plus bas. Ces deux listes sélectionnées seront par la suite appelées “parents”. Nous prélevons chez l’un des 2 parents un segment de villes (les 2 extrémités du segment étant choisies aléatoirement). A partir de ce segment, nous créons un enfant à partir des villes du deuxième parent. Néanmoins, il se peut que certaines villes ne soient pas affectées après ce “changement d’ADN”, et donc nous devons ajouter les villes manquantes.
Si l’enfant ainsi produit à un coût global plus bas que la ville ayant le coût le plus haut parmi les dix “ancêtres” (les villes de départ) ET que l’enfant est différent de ses deux parents (dans l’ordre de ses villes) alors, nous remplaçons la ville la “moins efficace” (celle avec le coût le plus élevé) par l’enfant.
A partir de cette nouvelle population, on peut relancer l’algorithme autant de fois qu’on le désire pour raffiner notre population, peut être jusqu’à n’avoir plus que des enfants.


	VI. Mutation
A partir d’une liste de villes, grâce au taux de mutation donné en paramètre, on effectue un certain nombre de Swap (proportionnel au taux de mutation) pour changer l’ordre des villes tel une mutation.


	VII. Pareto - online/offline
Ces algorithmes sont des filtres qui permettent de retirer les solutions dominées afin qu’il ne reste que les solutions non-dominées.
- La version Online consiste à ajouter dans un ensemble vides des solutions qui ne sont pas connues à l’avance. Chaque solution mise en entrée est comparée à l’existant. Si les solutions existantes dans la liste des non-dominées sont dominées par la solution entrante, alors elles sont supprimées et la solution entrante est ajoutée à la liste des non-dominées, si, la solution se différencie juste des autres non-dominées sans ni les dominer ni être dominée alors elle ajoutée à la liste des solutions non-dominées mais aucune des existantes n’est supprimée. Sinon, la solution entrante est ajoutée à la liste des solutions dominées.
- La version Offline, la seule différence avec la version Online est que l’on connaît directement toutes les solutions que nous voulons filtrer et on garde toutes celles qui sont non-dominées (avec les mêmes règles de filtrage)


