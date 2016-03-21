package d7002d.laboration4.Connectables;

public interface Connectable {
	/**
	 * Connect this entity to some specific other entity.
	 * 
	 * @param entity
	 *            The entity to check for connection to.
	 */
	public abstract void connect(Connectable entity);

	/**
	 * Disconnect some specific other entity from this entity, if connected.
	 * 
	 * @param entity
	 *            The entity to disconnect.
	 */
	public abstract void disconnect(Connectable entity);

	/**
	 * Disconnect all entities from this entity.
	 */
	public abstract void disconnectAll();

	/**
	 * Get the identifier of this entity.
	 * 
	 * @return The identifier.
	 */
	public abstract String getId();


	/**
	 * Tell whether the entity is connected to some specific other entity.
	 * 
	 * @param entity
	 *            The entity to check for connection to.
	 * @return true if connected, false otherwise.
	 */
	public abstract boolean isConnected(Connectable entity);
}
