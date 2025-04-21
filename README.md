# PayTrack – Personal Finance Tracking App
#### Video Demo: https://www.youtube.com/shorts/09pc7yi77s0

#### Description:

**PayTrack** is a modern Android application designed to help users easily manage and track their personal finances. With an intuitive and user-friendly interface built using **Jetpack Compose**, PayTrack provides users with the ability to log expenses, monitor financial habits, and stay on top of their spending — all in one clean, secure app.

The core idea behind PayTrack is to simplify the process of financial tracking while ensuring the app remains secure, fast, and pleasant to use. Whether you're looking to track daily spending, visualize your monthly expenses through interactive charts, or just keep a record of your purchases, PayTrack is designed to meet those needs with simplicity and style. It’s built with real user behavior in mind — minimal steps to add data, useful insights without overwhelming graphs, and a reliable sync experience powered by Firebase.

---

### 🔧 Technologies Used:

- **Kotlin** – For a clean, expressive, and modern Android codebase.
- **Jetpack Compose** – For building beautiful, declarative UIs with less boilerplate and more flexibility.
- **Firebase Authentication** – For managing secure user login, signup, and Google sign-in options.
- **Firebase Firestore** – To store and manage user financial data in real-time.
- **Firebase Password Reset Link** – Allowing users to change their passwords via secure email links.
- **Dagger Hilt** – For dependency injection, keeping code scalable, modular, and testable.
- **Android DataStore** – For storing small pieces of data locally, such as user preferences, display options, and last-selected filters.

---

### 💡 Features Overview:

- **Sign Up / Sign In / Google Auth:**  
  Users can create an account using email/password or sign in directly with their Google account. Firebase Authentication ensures the process is seamless and secure. Google sign-in helps reduce barriers to entry, while traditional login is ideal for privacy-conscious users.

- **Charts Screen:**  
  A visual breakdown of income and expenses with dynamic charts. Users can see trends, totals, and changes over time, helping them make more informed financial decisions. It includes pie charts and bar graphs for better insight.

- **Profile Screen:**  
  Users can view their profile, change personal settings, and manage authentication preferences. It’s also the central place to update login credentials or initiate account deletion.

- **Password Change via Firebase Link:**  
  If a user wants to change their password, a secure reset link is sent to their email, ensuring a smooth and secure recovery process without exposing user data.

- **Delete Account:**  
  Users have full control over their data. They can choose to permanently delete their account and all associated data. The deletion process is irreversible and done via Firebase functions.

- **Data Manipulation & Persistence:**  
  Users can add, edit, and delete expense/income entries. Data is stored in Firestore and synced in real-time. DataStore is used for storing app-level settings such as theme preferences and selected currency.

---

### 📁 File and Code Structure:

- `MainActivity.kt`:  
  The entry point of the application, managing navigation and the overall UI setup.

- `ui/`:  
  Contains all the Jetpack Compose UI files for screens like SignIn, SignUp, Home, Profile, and Charts.

- `auth/`:  
  Handles all Firebase Authentication logic — sign up, login, Google sign-in, password reset, and account deletion.

- `data/`:  
  Contains the repository and local storage logic using Firestore and DataStore. Also includes helper classes for managing financial transactions.

- `di/`:  
  Dagger Hilt setup for injecting dependencies across the app.

- `model/`:  
  Defines data models like `User`, `Transaction`, etc., to structure and manage app data cleanly.

- `viewmodel/`:  
  Includes ViewModels that bridge the UI and data layers, managing state and logic for each screen using Kotlin coroutines and state flows.

---

### 🧠 Design Decisions:

1. **Jetpack Compose Over XML:**  
   Opted for Jetpack Compose to take advantage of modern Android UI practices and reduce boilerplate. This also made it easier to create reactive UIs and manage state cleanly. Layouts became easier to preview and modify without XML files.

2. **Dagger Hilt for Dependency Injection:**  
   Choosing Hilt allowed for cleaner architecture and better testability. Injecting repositories, use cases, and other components across ViewModels was much easier and reduced manual setup.

3. **Firebase Integration:**  
   Firebase services offered a fast, secure, and scalable backend for authentication and real-time data syncing, making them ideal for a finance app. It also saved development time with ready-made auth flows and robust security.

4. **Use of Android DataStore:**  
   Replaced SharedPreferences with DataStore for local persistence to benefit from better Kotlin support and flow-based data access. Settings like theme, currency, and sort preferences are persisted through DataStore.

5. **Real-Time Data Sync:**  
   Firestore enables real-time updates. For a finance app where numbers constantly change, this provides a much better user experience. Users instantly see updated balances and charts after every transaction.

---

### ✅ What’s Next:

There are several features that could be added in the future, including:

- Budget planning and notifications for over-spending.
- Exporting transaction data to CSV or PDF format.
- Integration with banking APIs to automatically fetch transaction data.
- Multi-currency and localization support.
- Dark mode toggle and more customization options.

---

### 🚀 Final Thoughts:

PayTrack was built to demonstrate not just Android development skills, but also attention to detail in design, architecture, and user experience. From Firebase integration to advanced state handling with ViewModels and Compose, every decision was made to create a product that is both practical and enjoyable to use.

The project combines best practices in modern Android development and real-world usability. It’s a solid foundation that could grow into a full-fledged finance assistant. We're proud of the results, and excited about the possibilities ahead.

---

