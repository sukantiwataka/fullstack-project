import { useEffect, useState } from 'react'
import './App.css'

const api = async (path, options = {}) => {
  const response = await fetch(`/api${path}`, {
    headers: { 'Content-Type': 'application/json', ...options.headers },
    ...options,
  })
  if (!response.ok) {
    let message = `Request failed (${response.status})`
    try {
      const body = await response.json()
      message = body.message || body.detail || message
    } catch {
      // Keep the status-based message when the server has no JSON body.
    }
    throw new Error(message)
  }
  if (response.status === 204) return null
  const body = await response.json()
  return body.data
}

const emptyRestaurant = { name: '', cuisine: '', address: '', phone: '' }
const emptyMenu = { restaurantId: '', name: '', description: '', category: 'Main course', price: '', available: true }
const emptyUser = { name: '', email: '', phone: '', role: 'Customer' }

function App() {
  const [tab, setTab] = useState('menu')
  const [restaurants, setRestaurants] = useState([])
  const [menuItems, setMenuItems] = useState([])
  const [users, setUsers] = useState([])
  const [restaurantForm, setRestaurantForm] = useState(emptyRestaurant)
  const [menuForm, setMenuForm] = useState(emptyMenu)
  const [userForm, setUserForm] = useState(emptyUser)
  const [editing, setEditing] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const load = async () => {
    setLoading(true)
    setError('')
    try {
      const [restaurantData, menuData, userData] = await Promise.all([
        api('/restaurants'),
        api('/menu-items'),
        api('/users'),
      ])
      setRestaurants(restaurantData)
      setMenuItems(menuData)
      setUsers(userData)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    load()
  }, [])

  useEffect(() => {
    if (!editing && restaurants.length && !menuForm.restaurantId) {
      setMenuForm((current) => ({ ...current, restaurantId: String(restaurants[0].id) }))
    }
  }, [restaurants, editing, menuForm.restaurantId])

  const reset = () => {
    setEditing(null)
    setRestaurantForm(emptyRestaurant)
    setMenuForm({
      ...emptyMenu,
      restaurantId: restaurants[0] ? String(restaurants[0].id) : '',
    })
    setUserForm(emptyUser)
  }

  const submitRestaurant = async (event) => {
    event.preventDefault()
    setError('')
    try {
      if (editing?.kind === 'restaurant') {
        await api(`/restaurants/${editing.id}`, { method: 'PUT', body: JSON.stringify(restaurantForm) })
      } else {
        await api('/restaurants', { method: 'POST', body: JSON.stringify(restaurantForm) })
      }
      reset()
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  const submitMenu = async (event) => {
    event.preventDefault()
    setError('')
    try {
      const payload = {
        ...menuForm,
        restaurantId: Number(menuForm.restaurantId),
        price: Number(menuForm.price),
      }
      if (editing?.kind === 'menu') {
        await api(`/menu-items/${editing.id}`, { method: 'PUT', body: JSON.stringify(payload) })
      } else {
        await api('/menu-items', { method: 'POST', body: JSON.stringify(payload) })
      }
      reset()
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  const submitUser = async (event) => {
    event.preventDefault()
    setError('')
    try {
      if (editing?.kind === 'user') {
        await api(`/users/${editing.id}`, { method: 'PUT', body: JSON.stringify(userForm) })
      } else {
        await api('/users', { method: 'POST', body: JSON.stringify(userForm) })
      }
      reset()
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  const remove = async (kind, id) => {
    if (!window.confirm(`Delete this ${kind}?`)) return
    setError('')
    try {
      await api(`/${kind === 'menu' ? 'menu-items' : kind + 's'}/${id}`, { method: 'DELETE' })
      reset()
      await load()
    } catch (err) {
      setError(err.message)
    }
  }

  const editMenu = (item) => {
    setEditing({ kind: 'menu', id: item.id })
    setMenuForm({
      restaurantId: String(item.restaurantId),
      name: item.name,
      description: item.description || '',
      category: item.category || 'Main course',
      price: String(item.price),
      available: item.available,
    })
  }

  const editRestaurant = (item) => {
    setEditing({ kind: 'restaurant', id: item.id })
    setRestaurantForm({
      name: item.name,
      cuisine: item.cuisine || '',
      address: item.address || '',
      phone: item.phone || '',
    })
  }

  const editUser = (item) => {
    setEditing({ kind: 'user', id: item.id })
    setUserForm({
      name: item.name,
      email: item.email,
      phone: item.phone || '',
      role: item.role || 'Customer',
    })
  }

  const restaurantName = (id) => restaurants.find((item) => item.id === id)?.name || `Restaurant #${id}`

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <span className="brand-mark">T</span>
          <div>
            <strong>TastyTable</strong>
            <small>Admin workspace</small>
          </div>
        </div>
        <nav className="nav">
          <button className={tab === 'menu' ? 'active' : ''} onClick={() => setTab('menu')}>
            <span>◫</span> Menu
          </button>
          <button className={tab === 'restaurants' ? 'active' : ''} onClick={() => setTab('restaurants')}>
            <span>⌂</span> Restaurants
          </button>
          <button className={tab === 'users' ? 'active' : ''} onClick={() => setTab('users')}>
            <span>♙</span> Users
          </button>
        </nav>
        <div className="server-status"><i /> API connected</div>
      </aside>

      <main>
        <header className="topbar">
          <div>
            <p className="eyebrow">Dashboard</p>
            <h1>{tab === 'menu' ? 'Menu management' : tab === 'restaurants' ? 'Restaurants' : 'Users'}</h1>
          </div>
          <button className="refresh" onClick={load} disabled={loading}>↻ Refresh</button>
        </header>

        {error && <div className="alert"><strong>Could not complete the request.</strong> {error}</div>}

        {loading ? (
          <div className="loading">Loading workspace...</div>
        ) : tab === 'menu' ? (
          <section className="workspace">
            <form className="panel form-panel" onSubmit={submitMenu}>
              <div className="panel-heading">
                <div><span>Menu item</span><h2>{editing?.kind === 'menu' ? 'Edit dish' : 'Add a dish'}</h2></div>
              </div>
              <label>Restaurant
                <select value={menuForm.restaurantId} onChange={(e) => setMenuForm({ ...menuForm, restaurantId: e.target.value })} required>
                  <option value="">Select restaurant</option>
                  {restaurants.map((restaurant) => <option key={restaurant.id} value={restaurant.id}>{restaurant.name}</option>)}
                </select>
              </label>
              <label>Dish name<input value={menuForm.name} onChange={(e) => setMenuForm({ ...menuForm, name: e.target.value })} placeholder="Truffle pasta" required /></label>
              <label>Category
                <select value={menuForm.category} onChange={(e) => setMenuForm({ ...menuForm, category: e.target.value })}>
                  {['Starter', 'Main course', 'Dessert', 'Beverage', 'Side'].map((value) => <option key={value}>{value}</option>)}
                </select>
              </label>
              <label>Price<input type="number" min="0.01" step="0.01" value={menuForm.price} onChange={(e) => setMenuForm({ ...menuForm, price: e.target.value })} placeholder="12.50" required /></label>
              <label>Description<textarea value={menuForm.description} onChange={(e) => setMenuForm({ ...menuForm, description: e.target.value })} placeholder="Short dish description" /></label>
              <label className="checkbox"><input type="checkbox" checked={menuForm.available} onChange={(e) => setMenuForm({ ...menuForm, available: e.target.checked })} /> Available</label>
              <div className="form-actions">
                {editing?.kind === 'menu' && <button type="button" className="secondary" onClick={reset}>Cancel</button>}
                <button type="submit">{editing?.kind === 'menu' ? 'Save changes' : 'Add item'}</button>
              </div>
            </form>

            <div className="panel table-panel">
              <div className="panel-heading">
                <div><span>Catalog</span><h2>{menuItems.length} menu items</h2></div>
              </div>
              <div className="table-wrap">
                <table>
                  <thead><tr><th>Dish</th><th>Restaurant</th><th>Category</th><th>Price</th><th>Status</th><th /></tr></thead>
                  <tbody>
                    {menuItems.map((item) => (
                      <tr key={item.id}>
                        <td><strong>{item.name}</strong><small>{item.description || 'No description'}</small></td>
                        <td>{restaurantName(item.restaurantId)}</td>
                        <td><span className="category">{item.category}</span></td>
                        <td>${Number(item.price).toFixed(2)}</td>
                        <td><span className={item.available ? 'status available' : 'status unavailable'}>{item.available ? 'Available' : 'Hidden'}</span></td>
                        <td className="row-actions"><button onClick={() => editMenu(item)} title="Edit">✎</button><button className="danger" onClick={() => remove('menu', item.id)} title="Delete">×</button></td>
                      </tr>
                    ))}
                    {!menuItems.length && <tr><td colSpan="6" className="empty">No menu items yet.</td></tr>}
                  </tbody>
                </table>
              </div>
            </div>
          </section>
        ) : tab === 'restaurants' ? (
          <section className="workspace">
            <form className="panel form-panel" onSubmit={submitRestaurant}>
              <div className="panel-heading"><div><span>Restaurant</span><h2>{editing?.kind === 'restaurant' ? 'Edit restaurant' : 'Add restaurant'}</h2></div></div>
              <label>Name<input value={restaurantForm.name} onChange={(e) => setRestaurantForm({ ...restaurantForm, name: e.target.value })} required /></label>
              <label>Cuisine<input value={restaurantForm.cuisine} onChange={(e) => setRestaurantForm({ ...restaurantForm, cuisine: e.target.value })} placeholder="Italian" /></label>
              <label>Address<input value={restaurantForm.address} onChange={(e) => setRestaurantForm({ ...restaurantForm, address: e.target.value })} /></label>
              <label>Phone<input value={restaurantForm.phone} onChange={(e) => setRestaurantForm({ ...restaurantForm, phone: e.target.value })} /></label>
              <div className="form-actions">{editing?.kind === 'restaurant' && <button type="button" className="secondary" onClick={reset}>Cancel</button>}<button type="submit">{editing?.kind === 'restaurant' ? 'Save changes' : 'Add restaurant'}</button></div>
            </form>
            <div className="panel card-grid-panel">
              <div className="panel-heading"><div><span>Locations</span><h2>{restaurants.length} restaurants</h2></div></div>
              <div className="cards">
                {restaurants.map((restaurant) => (
                  <article className="card" key={restaurant.id}>
                    <div className="card-icon">⌂</div>
                    <div className="card-body"><h3>{restaurant.name}</h3><p>{restaurant.cuisine || 'General cuisine'}</p><small>{restaurant.address || 'Address not set'}</small></div>
                    <div className="card-actions"><button onClick={() => editRestaurant(restaurant)}>Edit</button><button className="danger" onClick={() => remove('restaurant', restaurant.id)}>Delete</button></div>
                  </article>
                ))}
                {!restaurants.length && <div className="empty">No restaurants yet.</div>}
              </div>
            </div>
          </section>
        ) : (
          <section className="workspace">
            <form className="panel form-panel" onSubmit={submitUser}>
              <div className="panel-heading"><div><span>User account</span><h2>{editing?.kind === 'user' ? 'Edit user' : 'Add user'}</h2></div></div>
              <label>Name<input value={userForm.name} onChange={(e) => setUserForm({ ...userForm, name: e.target.value })} required /></label>
              <label>Email<input type="email" value={userForm.email} onChange={(e) => setUserForm({ ...userForm, email: e.target.value })} required /></label>
              <label>Phone<input value={userForm.phone} onChange={(e) => setUserForm({ ...userForm, phone: e.target.value })} /></label>
              <label>Role<select value={userForm.role} onChange={(e) => setUserForm({ ...userForm, role: e.target.value })}><option>Customer</option><option>Manager</option><option>Admin</option></select></label>
              <div className="form-actions">{editing?.kind === 'user' && <button type="button" className="secondary" onClick={reset}>Cancel</button>}<button type="submit">{editing?.kind === 'user' ? 'Save changes' : 'Add user'}</button></div>
            </form>
            <div className="panel table-panel">
              <div className="panel-heading"><div><span>Directory</span><h2>{users.length} users</h2></div></div>
              <div className="table-wrap">
                <table><thead><tr><th>User</th><th>Email</th><th>Phone</th><th>Role</th><th /></tr></thead><tbody>
                  {users.map((item) => <tr key={item.id}><td><strong>{item.name}</strong></td><td>{item.email}</td><td>{item.phone || '—'}</td><td><span className="category">{item.role}</span></td><td className="row-actions"><button onClick={() => editUser(item)}>✎</button><button className="danger" onClick={() => remove('user', item.id)}>×</button></td></tr>)}
                  {!users.length && <tr><td colSpan="5" className="empty">No users yet.</td></tr>}
                </tbody></table>
              </div>
            </div>
          </section>
        )}
      </main>
    </div>
  )
}

export default App
