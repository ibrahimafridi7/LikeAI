# LikeAI E-commerce App

## Setup

### API Keys
This project requires several API keys to function properly. For security reasons, these keys are not included in the repository.

1. Copy `local.properties.template` to `local.properties`
2. Add your API keys to `local.properties`:
   ```properties
   FIREBASE_API_KEY=your_firebase_api_key
   FIREBASE_APP_ID=your_firebase_app_id
   FIREBASE_PROJECT_ID=your_firebase_project_id
   FIREBASE_STORAGE_BUCKET=your_firebase_storage_bucket
   ```

### Firebase Setup
1. Create a new Firebase project
2. Add your Android app to the project
3. Download the `google-services.json` file
4. Place it in the `app/` directory
5. Add your API keys to `local.properties` as described above

Note: Never commit `local.properties` or `google-services.json` to version control! 