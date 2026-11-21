# TastyTable full-stack app

## Set up MySQL

Start your local MySQL server. In MySQL Workbench, create the application database:

```sql
CREATE DATABASE user_db;
```

The backend connects to `localhost:3306` and uses the `user_db` database. It defaults to username `root`; provide your local MySQL password through `DB_PASSWORD` rather than storing it in project files:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-local-mysql-password"
```

Keep these environment variables set in the same terminal when running the backend.

## Run the backend

```powershell
cd user-service/user-service
$env:JAVA_HOME="C:\Program Files\Java\jdk-25.0.5.0.3"
.\mvnw.cmd spring-boot:run
```

The API starts at `http://localhost:8080`. Hibernate creates or updates the tables in `user_db`.

## Run the frontend

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. Vite proxies `/api` requests to the Spring backend.
