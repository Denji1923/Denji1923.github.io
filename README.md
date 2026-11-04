# Campus Lost & Found System — Web Version (Java Servlets + JSP + MySQL)

This is your JavaFX desktop project converted into a **website**. It runs in the browser at
`http://localhost:8080/CampusLostFound/`. The look (navy / gold / crimson school theme), the pages,
the logins and the database are the same as before.

## 1. What you need

| Tool | Version | Notes |
|------|---------|-------|
| JDK | 17 or newer | JDK 21 is fine |
| NetBeans | 17 or newer | the normal Apache NetBeans download already has web support |
| **Apache Tomcat** | **9.0.x** (important!) | download the *Core → zip* from https://tomcat.apache.org/download-90.cgi and unzip it anywhere |
| MySQL | XAMPP's MySQL works | only MySQL is needed from XAMPP, **not** Apache |

> **Use Tomcat 9, not Tomcat 10 or 11.** Tomcat 10+ uses a different package name (`jakarta.servlet`)
> and this project (like most school projects) uses `javax.servlet`.

## 2. Start MySQL (XAMPP)

1. Open the XAMPP Control Panel (run as Administrator) and click **Start** next to **MySQL**.
2. That's all. **The website creates the database `campus_lost_found`, all tables and the default admin
   account by itself the first time it starts.**
   (You may still import `database/schema.sql` in phpMyAdmin if you prefer. Both ways work.)
3. If your MySQL has a root password, or runs on another port, edit `src/main/resources/db.properties`.

MySQL will not start? See "Troubleshooting" at the bottom.

## 3. Run it in NetBeans (Option A — recommended)

**One time only: add Tomcat to NetBeans**
1. Open the **Services** tab (Window → Services) → right-click **Servers** → **Add Server…**
2. Choose **Apache Tomcat or TomEE** → Next.
3. **Server Location:** browse to the folder you unzipped (the one that contains `bin`, `conf`, `webapps`).
4. Enter a username and password (for example `admin` / `admin`) and tick **Create user if it does not exist** → Finish.

**Every time**
1. **File → Open Project…** → choose the `CampusLostFoundWeb` folder (it shows a Maven icon).
2. Right-click the project → **Properties → Run**: make sure **Server** is *Apache Tomcat* and the
   **Java EE Version** is *Java EE 8 Web*. Click OK.
3. Right-click the project → **Clean and Build** (the first time you need internet so Maven can download the libraries).
4. Press **Run** (green ▶ / F6). NetBeans starts Tomcat, deploys the site and opens your browser at
   **http://localhost:8080/CampusLostFound/**

If the browser opens the wrong address, type it yourself: `http://localhost:8080/CampusLostFound/`

## 4. Option B — no Tomcat installation (Maven + Jetty)

Right-click the project → **Run Maven → Goals…** → type `jetty:run` → OK.
Then open **http://localhost:8080/CampusLostFound/**. Press the red stop square in the Output window to stop it.
*(This option was not tested by the author of this conversion. Use Option A if it gives trouble.)*

## 5. Option C — copy the .war to any Tomcat 9

1. **Clean and Build** in NetBeans. The file `target/CampusLostFound.war` is created.
2. Copy it into Tomcat's `webapps` folder and start Tomcat (`bin/startup.bat`).
3. Open `http://localhost:8080/CampusLostFound/`.

## 6. Logins

| Portal | Login |
|--------|-------|
| Admin | username `admin`, password `admin123` (created automatically on first start) |
| Student | any number like `25-22-081` + full name. New students are registered on first login; later logins must use the same name. |

## 7. How the old desktop code became a website

| Desktop (JavaFX) | Web version |
|------------------|-------------|
| `Launcher`, `MainApp`, `SceneManager` | `web/AppStartupListener` (starts the database) + Tomcat |
| `WelcomeView`, `*LoginView`, `*Page` (JavaFX windows) | `.jsp` pages in `src/main/webapp/` and `WEB-INF/views/` |
| `Shell`, `StudentShell`, `AdminShell` (abstract + polymorphism) | `web/BaseServlet` (abstract) + `WEB-INF/tags/layout.tag` (banner + menu) |
| each `*Page` class | one servlet in `web/`, for example `ViewItemsServlet` |
| `style.css` (JavaFX CSS) | `webapp/assets/css/style.css` (normal CSS, same colours) |
| `Session` (one static user) | `HttpSession` — **every visitor has their own login**; a static user would be shared by everyone on a website |
| CSV export to a file (`FileChooser`) | a **Export CSV** button that downloads the file in the browser |
| `Ui.confirm` | JavaScript `confirm()` on the Delete button |

**Unchanged:** `model`, `exception`, `util`, `dao`, `service` (all OOP lessons: inheritance, abstract classes,
interfaces, polymorphism, generics, collections, exceptions, JDBC). Small additions: `ApprovalStatus` / `ItemType` have a
`getCssClass()`, `StudentSummary` replaces the JavaFX `record`, `ReportService.exportCsv` can write to a browser,
and `DatabaseInitializer` creates the tables automatically.

### Pages and addresses

| Address | Page |
|---------|------|
| `/` | Welcome — choose Student or Admin |
| `/student/login`, `/student/home`, `/student/report`, `/student/items`, `/student/claim` | Student portal |
| `/admin/login`, `/admin/home`, `/admin/transactions`, `/admin/items`, `/admin/claims`, `/admin/students` | Admin portal |

### Security built in
* Pages under `/student/*` and `/admin/*` redirect to the login page unless the right role is logged in (`SecurityFilter`).
* Every form carries a secret CSRF token that is checked on submit.
* All text typed by users is HTML-escaped (`<c:out>`), so `<script>` typed into a form cannot run.
* A new session id is created at login; session cookies are HttpOnly; private pages are not cached.
* CSV export protects against Excel formula injection.

## 8. Troubleshooting

**MySQL does not start in XAMPP**
* *Port 3306 in use:* press `Win + R`, run `services.msc`, stop any other **MySQL / MySQL80 / MariaDB** service, set it to Manual, start MySQL in XAMPP again.
  Or change both `port=3306` lines in XAMPP → MySQL → Config → `my.ini` to `3307` and use `3307` in `db.properties`.
* *"MySQL shutdown unexpectedly":* open Logs → `mysql_error.log`. A common fix: stop MySQL, rename `C:\xampp\mysql\data` to `data_old`,
  copy `C:\xampp\mysql\backup` to a new `data` folder, copy your `campus_lost_found` folder and `ibdata1` from `data_old` into it, start MySQL.

**The page says "Cannot connect to MySQL…"** — MySQL is not running, or `db.properties` has the wrong port / password. Start MySQL, then just reload the page (no need to restart the site).

**NetBeans: no server in the list / "Server not set"** — do the one-time "Add Tomcat" step in section 3.

**`HTTP Status 404`** — check the address ends with `/CampusLostFound/`. If it is `localhost:8080/` only, you see Tomcat's own page.

**`HTTP Status 500`, "The absolute uri: http://java.sun.com/jsp/jstl/core cannot be resolved"** — the libraries were not downloaded. Right-click the project → **Clean and Build** (with internet), then Run again.

**`ClassNotFoundException: javax.servlet...` or `jakarta`-related errors** — you are using Tomcat 10/11. Install Tomcat **9**.

**`release version 17 not supported` / `invalid target release`** — NetBeans is using an old JDK. Tools → Java Platforms, add JDK 17+, then Project Properties → Build → Compile → Java Platform.

**`Address already in use: 8080`** — another program uses port 8080. Stop it, or change Tomcat's port in NetBeans: Services → Servers → Tomcat → right-click Properties → Connection → Server Port.

**"Your page expired or the security token was missing"** — the login session timed out (30 minutes). Reload the page and try again.

**Where is the activity log?** `[Tomcat folder]/logs/campus-activity.log` (and the *Transactions* page in the admin portal).

**Reset everything** — in phpMyAdmin drop the `campus_lost_found` database, then restart the site: it recreates everything.

## 9. Putting it on the internet

The site needs a host that runs **Java (Tomcat 9)** plus a **MySQL** database. Ordinary PHP-only hosting cannot run it.
The project includes a `Dockerfile` (builds the .war and runs it on Tomcat 9, served at `/`) and a `render.yaml`.

**Easiest route (Render + a hosted MySQL)**
1. Put the project folder in a GitHub repository.
2. Create a MySQL database somewhere that gives you a public host (Aiven, Railway, PlanetScale-compatible MySQL, etc.) and note host, port, user, password.
3. On https://render.com choose **New → Blueprint** (or **New → Web Service → Docker**) and select your repository.
4. Set these environment variables:

| Variable | Example |
|----------|---------|
| `DB_URL` | `jdbc:mysql://HOST:PORT/campus_lost_found?sslMode=REQUIRED&serverTimezone=UTC` |
| `DB_USER` | your database user |
| `DB_PASSWORD` | your database password |
| `ADMIN_PASSWORD` | a strong password for the first admin (user `admin`) |
| `PORT` | `8080` |

5. Deploy. The site appears at `https://your-service.onrender.com/` and creates its tables by itself on the first start.
   (If your database user cannot create databases, create an empty `campus_lost_found` database first.)

Any other Docker host (Railway, Fly.io, a VPS) works the same way; on a plain VPS you can also copy `CampusLostFound.war` into Tomcat 9's `webapps`.
Give the database details with environment variables (`DB_URL`, `DB_USER`, `DB_PASSWORD`) or Java options (`-Ddb.url=... -Ddb.user=... -Ddb.password=...`).

Before going public:
* **Set `ADMIN_PASSWORD` before the first start.** The admin is created only once, when the admins table is empty; if you forget, the default `admin / admin123` is used.
* **Keep uploaded pictures on a persistent disk** (the Blueprint mounts one at `/data`). Without one, pictures are lost at every redeploy.
* Use **HTTPS** (Render and most hosts provide it).
* The student login has **no password** (same as your original design), so anyone who knows a student's number and full name can log in as that student. That is fine for a school demo or a campus network, but not for private data.


## Picture attachments

Students can attach one picture (JPG, PNG, GIF or WEBP, up to 5 MB) on **Report Item**. Admins see a thumbnail in
**Approve / Reject Items**; clicking it opens the full-size picture (press Esc or click outside to close).

* Pictures are saved in `[your user folder]/CampusLostFoundUploads` (outside the website, so redeploying does not erase them).
  To use another folder, start Tomcat with `-Dlostfound.upload.dir=D:/lostfound-pictures` or set the `LOSTFOUND_UPLOAD_DIR` environment variable.
* Existing databases are upgraded automatically: the app adds the `items.attachment` column the first time it starts.
* Deleting an item also deletes its picture file.
