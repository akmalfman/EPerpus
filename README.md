# EPerpus

EPerpus is a digital library application that allows users to log in and register as either users or admins. Users can borrow books, while admins have additional capabilities including adding books with photos, editing and deleting books, and sorting books. The application uses Firebase as its database backend.

## Key Features
- **User:**
  - Login and registration
  - Borrowing books

- **Admin:**
  - Login and registration
  - Adding books with photos
  - Editing books
  - Deleting books
  - Sorting books

## Technologies Used
- **Backend:** Firebase

## Firebase Setup (Valid until June 24, 2024)
After June 24, 2024, follow these steps to configure Firebase:

1. **Create a Firebase Project:**
   - Go to the Firebase Console.
   - Click on "Add Project" and follow the setup instructions.
   - Download the `google-services.json` file and replace the existing file in the project.

2. **Set Up Authentication:**
   - In the Firebase Console, navigate to "Authentication" and enable the "Email/Password" sign-in method.

3. **Set Up Realtime Database:**
   - Go to "Database" in the Firebase Console.
   - Create a new Realtime Database.
   - Copy the database URL and replace it in all relevant links within the project (inside the activity files).

4. **Set Up Firebase Storage:**
   - In the Firebase Console, navigate to "Storage".
   - Create a new storage bucket.
   - This will be used to store book photos when adding new books.
