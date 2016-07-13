public class DoctorS implements DoctorIF {
private int id; /* Identificador univoco del Doctor */
private AcademiaS academia; /* Academia a la que pertenece el Doctor */

//este constructor me sirve para crear una academia nueva cuando estamos leiendo una nueva linea de datos
public DoctorS(int iD2, AcademiaIF a, DoctorIF doctor) {
	
	this.academia=(AcademiaS) a; //es equivalente a decir el doctor es de la academia 
	
	this.id=iD2; //definimos el valor de id en el constructor
	//creamos una nueva academia, con este doctor como doctor superiro
	AcademiaIF nueva=new AcademiaS(this);
	//insertamos la nueva academia en la estructura de datos de arboles de academia
	((AcademiaS) a).insertarNodo(doctor,nueva);
	}
	
//este constructor me sirve para hacer las consultas
public DoctorS() {
	// TODO Auto-generated constructor stub
}
//este constructor me sirve para crear el fundador, el fundador ya tiene academia, que es la academia global
public DoctorS(int iD2) {
	this.id=iD2;
}
/* Consulta el director de Tesis del doctor */
/* @returns el Doctor que fue su director de Tesis */
/* null en caso de que sea el fundador de la Academia */
public DoctorIF getSupervisor(){
	
	//si tenemos el supervisor ya no hace falta mirar mas
	if(this.getId()==academia.getFounder().getId()) return null;
	else{
		DoctorIF d=getSupervisorR(academia.getAcademias().getChildren());
		return d;
			}
	
	
	}
private DoctorIF getSupervisorR(ListIF<TreeIF<AcademiaIF>> children) {
	//caso trivial, si la lista esta vacia es que no tenemos el supervisor
	if(children.isEmpty()) return null;
	//creamos una nuvea lista que vamos a devolver recursivamente
	ListIF<TreeIF<AcademiaIF>> newList=new List<TreeIF<AcademiaIF>>();
	// creamos un iterador de la lista
	IteratorIF<TreeIF<AcademiaIF>> itl=children.iterator();
	while(itl.hasNext()){
		TreeIF<AcademiaIF> nodo=itl.getNext();
		//miramos los hijos de nodo
		ListIF<TreeIF<AcademiaIF>> nodosHijos=nodo.getChildren();
		//creamos un iterador para nodosHijos
		IteratorIF<TreeIF<AcademiaIF>> itlhijos=nodosHijos.iterator();
		//si la lista de hijos esta nuestro doctor
		while(itlhijos.hasNext()){
			TreeIF<AcademiaIF> hijoNext=itlhijos.getNext();
			if(((AcademiaS) hijoNext.getRoot()).getSuperiorAcademia().getId()==this.getId()) {
				return ((AcademiaS) nodo.getRoot()).getSuperiorAcademia();
		}
			newList.insert(hijoNext, 1);
		}
		
	}
	
	return getSupervisorR(newList);
	
}

@Override
public CollectionIF<DoctorIF> getAncestors(int generations) {
	// lo planteo como un proceso recursivo de getSupervisor, get estudiantes de este supervisor
	//empiezo con el doctor this y voy haciendo recursivamente.
	StackIF<DoctorIF> pila=new Stack<DoctorIF>();
	pila=getAncestorsRecursivo(this,pila, generations);
	return pila;
}
private StackIF<DoctorIF> getAncestorsRecursivo(DoctorIF doctorS, StackIF<DoctorIF> cola, int generations) {
	// caso trivial
	DoctorIF supe;
	if(generations==0||doctorS.getId()==academia.getFounder().getId()) return cola;
	else{
		
		supe=((DoctorS) doctorS).getSupervisor();
		cola.push(supe);;
		
	}
	
	generations--;
	
	
	
	return getAncestorsRecursivo(supe, cola, generations);
	
	
	
}

@Override
public CollectionIF<DoctorIF> getStudents() {
	//creamos un iterador del arbol
	//creamos un iterador de academia
			IteratorIF<AcademiaIF> its1=academia.getAcademias().iterator(0);
			its1.getNext();//pasamos el primer nodo que es nullo
			while(its1.hasNext()){
				AcademiaIF academ=its1.getNext();
				if(((AcademiaS) academ).getSuperiorAcademia().getId()==this.getId()) return ((AcademiaS) academ).getEstudinatesAcademiaIF();
				}
	return null; //llegado a este punto decimos que nullo
}

public CollectionIF<DoctorIF> getDescendants(int generations) {
	//creamos una pila de academias
	QueueIF<DoctorIF> cola=new Queue<DoctorIF>();
	//cogemos la primera lista de estudiantes del subarbol si no esta vacia
	TreeIF<AcademiaIF> subarbol=academia.getSubarbol(this);
	ListIF<DoctorIF> lista1=((AcademiaS) subarbol.getRoot()).getEstudinatesAcademiaIF();
	//sumamos los resultados
	IteratorIF<DoctorIF> itd=lista1.iterator();
	while(itd.hasNext()){
		cola.enqueue(itd.getNext());
	}
	generations--;
	//iteramos el arbol por niveles
	cola=getDescendantsR(subarbol.getChildren(),cola,generations);
	return cola;
}

private QueueIF<DoctorIF> getDescendantsR(ListIF<TreeIF<AcademiaIF>> children,
		QueueIF<DoctorIF> cola, int generations) {
	// caso trivial
	if(generations==0||children.isEmpty()) return cola;
	else{
		//creamos una nueva lista
		ListIF<TreeIF<AcademiaIF>> nuevaChildren=new List<TreeIF<AcademiaIF>>();
		//iteramos a children
		IteratorIF<TreeIF<AcademiaIF>> itchildren=children.iterator();
		while(itchildren.hasNext()){
			TreeIF<AcademiaIF> nChildren=itchildren.getNext();
			//sumamos a cola si tenemos estudinates en nChildren
			ListIF<DoctorIF> estudiantes=((AcademiaS) nChildren.getRoot()).getEstudinatesAcademiaIF();
			if(!estudiantes.isEmpty()){
				for(int i=1;i<=estudiantes.size();i++){
					cola.enqueue(estudiantes.get(i));
					
				}
			}
			
			ListIF<TreeIF<AcademiaIF>> nextLevel=nChildren.getChildren();
			//itermaos de nuevo la lista, para copiar en la nueva lista es siguiente nivel
			IteratorIF<TreeIF<AcademiaIF>> itf=nextLevel.iterator();
			while(itf.hasNext()){
				int size=nuevaChildren.size()+1;
				nuevaChildren.insert(itf.getNext(), size);
			}
		}
			generations--; //actualizamos el contador de generaciones y hacemos el return
			return getDescendantsR(nuevaChildren, cola, generations);
		
	}
	
	
}


@Override
public CollectionIF<DoctorIF> getSiblings() {
	// primero buscamos el doctor superior
	QueueIF<DoctorIF> resultado=new Queue<DoctorIF>();
	DoctorIF sup=this.getSupervisor();
	ListIF<DoctorIF> estudiantes=(ListIF<DoctorIF>) sup.getStudents();
	for(int i=1;i<=estudiantes.size();i++){
		if(estudiantes.get(i).getId()!=this.getId()){
		resultado.enqueue(estudiantes.get(i));	
		}
	}

	return resultado;
	
}
@Override
/**
 * Este m�todo nos sirve para devolver el id del doctor
 */
public int getId() {
	// TODO Auto-generated method stub
	return id;
}

/**
 * Sirve para iniciar la academiaS
 * @param a
 */
public void setAcademia(AcademiaS a) {
	// TODO Auto-generated method stub
	this.academia=a;
	
	
}


}