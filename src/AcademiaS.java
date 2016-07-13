
public class AcademiaS implements AcademiaIF {
	
	//AcademiaS es un arbol de academiaIF y en cada nodo de este arbol tenemos una lista de
	//doctores estudiantes que tiene un mismo doctor superior
	
	
	private TreeIF<AcademiaIF> academias;
	
	private ListIF<DoctorIF> estudiantesAcademiaIF; //arbol de estudiantes
	
	private DoctorIF superior; // es el doctor superior de un grupo de estudiantes
	
	
	//este metodo devuelve la lista de estudiantes de un nodo del arbol de la academia
	public ListIF<DoctorIF> getEstudinatesAcademiaIF() {
		return estudiantesAcademiaIF;
	}
	//este metodo devuelve el superior de la lista de estudiantes de un nodo del arbol de la academia
	public DoctorIF getSuperiorAcademia(){
		return superior;
	}
	//este metodo devuelve el arbol de mi academiaS formado por academiaIF
	public TreeIF<AcademiaIF> getAcademias() {
		return academias;
	}

	//en el constructor simple solamente creamos el arbol de academias
	public AcademiaS(){
		academias=new Tree<AcademiaIF>();
		
	}
	
	//constructor cuando creamos una nueva academia a partir de un doctor que va 
	// a poder ser superior de futuros doctores
	public AcademiaS(DoctorIF sup){
		estudiantesAcademiaIF=new List<DoctorIF>(); //inicializamos la lista de estudiantes, inicialmente vacia
		this.superior=sup; //damos valor a superior en la academia
	}

	@Override
	public boolean isEmpty() {
		// si el arbol de mi academia esta vacío entonces devolvemos verdadero
		if(academias.isEmpty()) return true;
		//podemos afinar un poco mas. Si no tenemos ningun estudiante entonces la academia también esta vacia
		TreeIF<AcademiaIF> primera=academias.getChild(1); //primera es el primer nodo del arbol
		//si la lista de estudiantes del primer nodo del arbol esta vacia, entonces es que no tenemos estudiantes
		if(((AcademiaS) primera.getRoot()).getEstudinatesAcademiaIF().isEmpty()) return true;
		return false; //En cualquier otra situacion devolvemos falso
	}
 
	@Override
	/**
	 * Este método me devuelve true si existe el doctor en mi academia
	 */
	public boolean contains(DoctorIF e) { 
		//iteramos todo el arbol de la academiaS y buscamos si tenemos el doctor e en alguna lista de mis estudiantes
		//academiaIF
		IteratorIF<AcademiaIF> ita=academias.iterator(1);
		while(ita.hasNext()){
			// buscamos en cada AcademiaIF si la lista de estudiantes contiene el doctor e
			AcademiaIF nextA=ita.getNext();
			if(((AcademiaS) nextA).getEstudinatesAcademiaIF().contains(e)) return true;
		}
		return false; // si llegamos aqui es porque no existe el doctor e
		
	}

	@Override
	public void clear() {
		// Borramos el arbol de academias
		academias.clear();
	}

	@Override
	public IteratorIF<DoctorIF> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DoctorIF getFounder() {
		// buscamos el primer nodo hijo de mi academia
		TreeIF<AcademiaIF> primero=academias.getChild(1);
		//devolvemos el doctor superior de mi primera academiaIF
		return ((AcademiaS) primero.getRoot()).getSuperiorAcademia();
	}

	@Override
	public DoctorIF getDoctor(int id) {
		//creamos un iterador de la academia por niveles
		IteratorIF<AcademiaIF> it=academias.iterator(2);
		while(it.hasNext()){
			AcademiaIF itgetDoctor=it.getNext();
			ListIF<DoctorIF> lista=((AcademiaS) itgetDoctor).getEstudinatesAcademiaIF();
			//iteramos la lista de estudiantes por encontrar el doctor id, si lo encontramos lo devolvemos
			IteratorIF<DoctorIF> getDoc=lista.iterator();
			while(getDoc.hasNext()){
				DoctorIF d=getDoc.getNext();
				if(d.getId()==id) return d; 
			}
		}
		return null; //llegado a este punto es que no tenemos el doctor en la academia
		
	}

	@Override
	public int size() {
		int i=0; //es el contador de del numero total de doctores
		//creamos un iterador de la academia por niveles
		IteratorIF<AcademiaIF> it=academias.iterator(2);
		while(it.hasNext()){
			AcademiaIF itgetDoctor=it.getNext();
			ListIF<DoctorIF> lista=((AcademiaS) itgetDoctor).getEstudinatesAcademiaIF();
			//iteramos la lista de estudiantes por encontrar el doctor id, si lo encontramos lo devolvemos
			IteratorIF<DoctorIF> getDoc=lista.iterator();
			while(getDoc.hasNext()){
				i++;//actualizamos el valor de contador
				getDoc.getNext();
			}
		}
		return i; //llegado a este punto es que no tenemos el doctor en la academia
		
	}

	@Override
	public void addDoctor(DoctorIF newDoctor, DoctorIF supervisor) {
	
		// TODO Auto-generated method stub
		if(this.contains(supervisor)&&!this.contains(newDoctor)){
			
			//iteramos nuestro arbol hasta encontrar el nodo que tiene el doctor supervisor
			IteratorIF<AcademiaIF> ita=academias.iterator(0);
			ita.getNext();
			while(ita.hasNext()){
				AcademiaIF nodo=ita.getNext();
				if(((AcademiaS) nodo).getSuperiorAcademia().getId()==supervisor.getId()){
					int size=((AcademiaS) nodo).getEstudinatesAcademiaIF().size()+1;
					((AcademiaS) nodo).getEstudinatesAcademiaIF().insert(newDoctor, size);
				}
			}
	
		}
	}
	
	@Override
	public void addSupervision(DoctorIF student, DoctorIF supervisor) {
		
		
	}

	public void setFounder(DoctorS founder) {
		//creamos una academia con un doctor superior nullo y founder en la lista de estudiantes
		AcademiaIF nueva0=new AcademiaS(null);
		((AcademiaS) nueva0). getEstudinatesAcademiaIF().insert(founder, 1);
		//insertamos el fundador como estudiante de la raiz de academias
		academias.setRoot(nueva0);
		// insertamos la primera academiaIF en mi arbol de academias
		AcademiaIF nueva=new AcademiaS(founder); //creamos una nueva academia
		TreeIF<AcademiaIF> temp=new Tree<AcademiaIF>(); //creamos un nuevo nodo
		temp.setRoot(nueva); //inicializamos en nuevo nodo con la academiaIF nueva
		academias.addChild(1,temp); //insertamos la nueva academia como el primer hijo de academias
	
	}
	/**
	 * Este procedimiento me sirve para insertar una nueva academia en mi arbol de academias
	 * @param s, es el doctor superior de una academiaIF
	 * @param nueva, es la academia que queremos insertar
	 */
	public void insertarNodo(DoctorIF s, AcademiaIF nueva){
		//tenemos que insertar en el arbol de academias en la posicion correcta, por lo tanto
		//utilizamos un procedimiento privado recursivo
				TreeIF<AcademiaIF> nodoAinsertar=EncontrarNodo(s,academias.getChildren());
				//si hemos encontrado un nodo donde insertar el nuevo nodo:
				if(nodoAinsertar!=null){TreeIF<AcademiaIF> nodo=new Tree<AcademiaIF>();
				nodo.setRoot(nueva);
				int size=nodoAinsertar.getChildren().size()+1;
				nodoAinsertar.addChild(size, nodo);
		
				}
		}
	
	/**
	 * Este procedimiento me sirve para insertar el Nodo en la posicion correcta.
	 * @param doctor
	 * @param lista
	 * @return
	 */
	private TreeIF<AcademiaIF> EncontrarNodo(DoctorIF doctor, ListIF<TreeIF<AcademiaIF>> lista) {
		//condicion de fin del método recursivo
		if(lista.isEmpty()) return null;
		else{
			//creamos una nueva lista que es la que vamos a devolver recursivamente
			ListIF<TreeIF<AcademiaIF>> nuevaLista=new List<TreeIF<AcademiaIF>>();
			//creamos un iterador para la lista
			IteratorIF<TreeIF<AcademiaIF>> it1=lista.iterator();
			while(it1.hasNext()){
				TreeIF<AcademiaIF> nodoT=it1.getNext();
				//si el arbol nodoT tiene el doctor superior
				if(((AcademiaS) nodoT.getRoot()).getSuperiorAcademia().getId()==doctor.getId()) {
						return nodoT;
				}
				ListIF<TreeIF<AcademiaIF>> ListaNext=nodoT.getChildren();
				IteratorIF<TreeIF<AcademiaIF>> nextIt=ListaNext.iterator();
				while(nextIt.hasNext()){
					int size=nuevaLista.size()+1;
					nuevaLista.insert(nextIt.getNext(), size);
					
				}
				
			}
			return EncontrarNodo(doctor, nuevaLista);
			
		}
	}
	public TreeIF<AcademiaIF>getSubarbol(DoctorIF d){
		
		TreeIF<AcademiaIF> t=EncontrarNodo(d,academias.getChildren());
		
		return t;
		
		
	}
		
}

