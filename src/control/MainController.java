package control;

import model.Edge;
import model.Graph;
import model.List;
import model.Vertex;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute

    //Referenzen
    private Graph allUsers;

    public MainController(){
        allUsers = new Graph();
        createSomeUsers();
    }

    /**
     * Fügt Personen dem sozialen Netzwerk hinzu.
     */
    private void createSomeUsers(){
        insertUser("Ulf");
        insertUser("Silent Bob");
        insertUser("Dörte");
        insertUser("Ralle");
        befriend("Silent Bob", "Ralle");
        befriend("Dörte", "Ralle");
    }

    /**
     * Fügt einen Nutzer hinzu, falls dieser noch nicht existiert.
     * @param name
     * @return true, falls ein neuer Nutzer hinzugefügt wurde, sonst false.
     */
    public boolean insertUser(String name){
        //TODO 05: Nutzer dem sozialen Netzwerk hinzufügen.
        if(name != null && allUsers.getVertex(name) == null){
            allUsers.addVertex(new Vertex(name));
            return true;
        }
        return false;
    }

    /**
     * Löscht einen Nutzer, falls dieser existiert. Alle Verbindungen zu anderen Nutzern werden ebenfalls gelöscht.
     * @param name
     * @return true, falls ein Nutzer gelöscht wurde, sonst false.
     */
    public boolean deleteUser(String name){
        //TODO 07: Nutzer aus dem sozialen Netzwerk entfernen.
        if(name != null && allUsers.getVertex(name) != null){
            allUsers.removeVertex(allUsers.getVertex(name));
            return true;
        }
        return false;
    }

    /**
     * Falls Nutzer vorhanden sind, so werden ihre Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @return
     */
    public String[] getAllUsers(){
        //TODO 06: String-Array mit allen Nutzernamen erstellen.

        if(!allUsers.getVertices().isEmpty()){
            int userNumber = 0;
            List<Vertex> allVertex = allUsers.getVertices();
            allVertex.toFirst();
            while (allVertex.hasAccess()){
                userNumber++;
                allVertex.next();
            }
            String[] userArray = new String[userNumber];
            allVertex.toFirst();
            for(int i = 0; allVertex.hasAccess();i++){
                userArray[i] = allVertex.getContent().getID();
                allVertex.next();
            }

            return userArray;
        }
        return new String[0];
    }

    /**
     * Falls der Nutzer vorhanden ist und Freunde hat, so werden deren Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @param name
     * @return
     */
    public String[] getAllFriendsFromUser(String name){
        //TODO 09: Freundesliste eines Nutzers als String-Array erstellen.
        if(allUsers.getVertex(name) != null){
            List<Vertex> neighbours = allUsers.getNeighbours(allUsers.getVertex(name));
            neighbours.toFirst();
            int neighbourNumber = 0;
            while (neighbours.hasAccess()){
                neighbourNumber++;
                neighbours.next();
            }
            String[] neighbourArray = new String[neighbourNumber];
            neighbours.toFirst();
            for(int i = 0; neighbours.hasAccess();i++){
                neighbourArray[i] = neighbours.getContent().getID();
                neighbours.next();
            }
            return neighbourArray;
        }
        return new String[0];
    }

    /**
     * Bestimmt den Zentralitätsgrad einer Person im sozialen Netzwerk, falls sie vorhanden ist. Sonst wird -1.0 zurückgegeben.
     * Der Zentralitätsgrad ist der Quotient aus der Anzahl der Freunde der Person und der um die Person selbst verminderten Anzahl an Nutzern im Netzwerk.
     * Gibt also den Prozentwert an Personen im sozialen Netzwerk an, mit der die Person befreundet ist.
     * @param name
     * @return
     */
    public double centralityDegreeOfUser(String name){
        //TODO 10: Prozentsatz der vorhandenen Freundschaften eines Nutzers von allen möglichen Freundschaften des Nutzers.
        if(allUsers.getVertex(name) != null) {
            double friend = getAllFriendsFromUser(name).length;
            double user = getAllUsers().length - 1;

            return friend/user;
        }
        return -1.0;
    }

    /**
     * Zwei Nutzer des Netzwerkes gehen eine Freundschaft neu ein, falls sie sich im Netzwerk befinden und noch keine Freunde sind.
     * @param name01
     * @param name02
     * @return true, falls eine neue Freundeschaft entstanden ist, ansonsten false.
     */
    public boolean befriend(String name01, String name02){
        //TODO 08: Freundschaften schließen.
        if(allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null && allUsers.getEdge(allUsers.getVertex(name01), allUsers.getVertex(name02)) == null) {
            Edge edge = new Edge(allUsers.getVertex(name01), allUsers.getVertex(name02), 0);
            allUsers.addEdge(edge);
            return true;
        }
        return false;
    }

    /**
     * Zwei Nutzer beenden ihre Freundschaft, falls sie sich im Netzwerk befinden und sie befreundet sind.
     * @param name01
     * @param name02
     * @return true, falls ihre Freundschaft beendet wurde, ansonsten false.
     */
    public boolean unfriend(String name01, String name02){
        //TODO 11: Freundschaften beenden.
        if(allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null && allUsers.getEdge(allUsers.getVertex(name01), allUsers.getVertex(name02)) != null) {
            allUsers.removeEdge(allUsers.getEdge(allUsers.getVertex(name01), allUsers.getVertex(name02)));
            return true;
        }
        return false;
    }

    /**
     * Bestimmt die Dichte des sozialen Netzwerks und gibt diese zurück.
     * Die Dichte ist der Quotient aus der Anzahl aller vorhandenen Freundschaftsbeziehungen und der Anzahl der maximal möglichen Freundschaftsbeziehungen.
     * @return
     */
    public double dense(){
        //TODO 12: Dichte berechnen.
        List<Edge> edgeList = allUsers.getEdges();
        double edges = 0;
        edgeList.toFirst();
        while(edgeList.hasAccess()){
            edges++;
            edgeList.next();
        }
        List<Vertex> vertexList = allUsers.getVertices();
        double vertex = 0;
        double vertexHelp = 0;
        vertexList.toFirst();

        while(vertexList.hasAccess()){
            vertex += vertexHelp;
            vertexHelp++;
            vertexList.next();
        }

        return edges/vertex;
    }

    /**
     * Gibt die möglichen Verbindungen zwischen zwei Personen im sozialen Netzwerk als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return
     */
    public String[] getLinksBetween(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 13: Schreibe einen Algorithmus, der mindestens eine Verbindung von einem Nutzer über Zwischennutzer zu einem anderem Nutzer bestimmt. Happy Kopfzerbrechen!
            if(getLinksBetweenRek(name01,name02).equals("")){
                return null;
            }
            return getLinksBetweenRek(name01,name02).split(",");
        }
        return null;
    }

    public String getLinksBetweenRek(String name01, String name02) {
        for(int i = 0; i<getAllFriendsFromUser(name01).length;i++){
            if(allUsers.getVertex(getAllFriendsFromUser(name01)[i]) == allUsers.getVertex(name02)){
                return name01 + "," + name02;
            }
        }
        allUsers.getVertex(name01).setMark(true);
        for(int j = 0;j < getAllFriendsFromUser(name01).length;j++){
            if(allUsers.getVertex(getAllFriendsFromUser(name01)[j]).isMarked() && !getLinksBetweenRek(getAllFriendsFromUser(name01)[j],name02).equals("")){
                return name01 + "," + getLinksBetweenRek(getAllFriendsFromUser(name01)[j],name02);
            }
        }
        allUsers.setAllVertexMarks(false);
        return "";
    }

    /**
     * Gibt zurück, ob es keinen Knoten ohne Nachbarn gibt.
     * @return true, falls ohne einsame Knoten, sonst false.
     */
    public boolean someoneIsLonely(){
        //TODO 14: Schreibe einen Algorithmus, der explizit den von uns benutzten Aufbau der Datenstruktur Graph und ihre angebotenen Methoden so ausnutzt, dass schnell (!) iterativ geprüft werden kann, ob der Graph allUsers keine einsamen Knoten hat. Dies lässt sich mit einer einzigen Schleife prüfen.
        List<Vertex> allVertices = allUsers.getVertices();
        while(allVertices.hasAccess()){
            if(getAllFriendsFromUser(allVertices.getContent().getID()).length == 0){
                return false;
            }
            allVertices.next();
        }
        return true;
    }

    /**
     * Gibt zurück, ob vom ersten Knoten in der Liste aller Knoten ausgehend alle anderen Knoten erreicht also markiert werden können.
     * Nach der Prüfung werden noch vor der Rückgabe alle Knoten demarkiert.
     * @return true, falls alle Knoten vom ersten ausgehend markiert wurden, sonst false.
     */
    public boolean testIfConnected(){
        //TODO 15: Schreibe einen Algorithmus, der ausgehend vom ersten Knoten in der Liste aller Knoten versucht, alle anderen Knoten über Kanten zu erreichen und zu markieren.
        List<Vertex> allVertices = allUsers.getVertices();
        if(!allVertices.isEmpty()) {
            Vertex first = allVertices.getContent();
            allVertices.next();
            while (allVertices.hasAccess()) {
                if (getLinksBetween(first.getID(), allVertices.getContent().getID()) == null){
                    allUsers.setAllVertexMarks(false);
                    return false;
                }
                allUsers.getVertex(allVertices.getContent().getID()).setMark(true);
                allVertices.next();
            }
        }
        allUsers.setAllVertexMarks(false);
        return true;
    }
    
    /**
     * Gibt einen String-Array zu allen Knoten zurück, die von einem Knoten ausgehend erreichbar sind, falls die Person vorhanden ist.
     * Im Anschluss werden vor der Rückgabe alle Knoten demarkiert.
     * @return Alle erreichbaren Knoten als String-Array, sonst null.
     */
    public String[] transitiveFriendships(String name){
        //TODO 16: Schreibe einen Algorithmus, der ausgehend von einem Knoten alle erreichbaren ausgibt.
        return null;
    }
    
    
    /**
     * Gibt eine kürzeste Verbindung zwischen zwei Personen des Sozialen Netzwerkes als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return Verbindung als String-Array oder null, falls es keine Verbindung gibt.
    */
    public String[] shortestPath(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 17: Schreibe einen Algorithmus, der die kürzeste Verbindung zwischen den Nutzern name01 und name02 als String-Array zurückgibt. Versuche dabei einen möglichst effizienten Algorithmus zu schreiben.
        }
        return null;
    }

}
