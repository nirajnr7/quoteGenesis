# Quote Genesis
 
Quote Genesis is an automated quote generation and social media posting system that creates inspirational quotes, generates beautiful images, and automatically posts them to Instagram. The application runs on a scheduled basis, making it perfect for maintaining an active social media presence with minimal manual intervention.
 
## 🌟 Features
 
- Automated quote fetching from external APIs
- Custom image generation with quotes
- Automatic image uploading to public hosting services
- Instagram integration for automated posting
- Configurable scheduling system
- Database persistence for tracking generated content
- RESTful API endpoints for manual control
 
## 🏗 Architecture
 
### High-Level Overview
 
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Quote API     │     │  Image Hosting  │     │   Instagram     │
│   (External)    │     │    Services     │     │     API        │
└────────┬────────┘     └────────┬────────┘     └────────┬────────┘
         │                       │                        │
         ▼                       ▼                        ▼
┌──────────────────────────────────────────────────────────────────┐
│                         Quote Genesis                             │
│                                                                  │
│  ┌─────────────┐     ┌─────────────┐     ┌─────────────┐        │
│  │   Quote     │     │   Image     │     │  Instagram  │        │
│  │  Service    │─────│  Generator  │─────│   Poster    │        │
│  └─────────────┘     └─────────────┘     └─────────────┘        │
│           │                 │                   │                 │
│           │                 │                   │                 │
│  ┌─────────────────────────────────────────────────────┐        │
│  │                     Scheduler                        │        │
│  └─────────────────────────────────────────────────────┘        │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```
 
### Component Description
 
1. **Quote Service**
   - Fetches quotes from external APIs
   - Stores quotes in the database
   - Manages quote selection and processing
 
2. **Image Generator**
   - Creates custom images with quotes
   - Handles font rendering and image composition
   - Manages temporary file storage
 
3. **Image Upload Service**
   - Uploads generated images to public hosting services
   - Supports multiple hosting providers (FreeImage, ImgHippo)
   - Manages URL tracking
 
4. **Instagram Poster**
   - Handles Instagram API integration
   - Manages post creation and publishing
   - Handles API authentication
 
5. **Scheduler**
   - Coordinates all components
   - Manages timing of operations
   - Provides error handling and logging
 
## 🚀 API Endpoints
 
### Quote Management
```http
GET /api/quotes/generate
```
- Generates a new quote and stores it in the database
- Response: Generated quote object
 
### Image Generation
```http
GET /api/generateImage
```
- Generates an image for the latest quote
- Uploads the image to hosting service
- Response: Success message with image URL
 
### Instagram Posting
```http
GET /api/postToInstagram
```
- Posts the latest generated image to Instagram
- Response: Success message with Instagram post ID
 
## ⚙️ Configuration
 
### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/quotegenesis
spring.datasource.username=postgres
spring.datasource.password=postgres
 
# Image Hosting API Keys
imgHippo.api.key=your_key_here
freeImage.api.key=your_key_here
 
# Instagram API Configuration
instagram.access.token=your_token_here
instagram.business.account.id=your_account_id
 
# Scheduler Configuration
quote.generation.cron=0 0 * * * *  # Run every hour
```
 
## 🛠 Technical Stack
 
- **Framework**: Spring Boot 2.x
- **Database**: PostgreSQL
- **Image Processing**: Java AWT/Graphics2D
- **External Services**:
  - FreeImage Host API
  - ImgHippo API
  - Instagram Graph API
- **Build Tool**: Maven
 
## 📦 Installation & Setup
 
1. Clone the repository:
```bash
git clone https://github.com/yourusername/quote-genesis.git
```
 
2. Configure application.properties with your API keys and database settings
 
3. Build the project:
```bash
mvn clean install
```
 
4. Run the application:
```bash
java -jar target/quote-genesis-1.0.0.jar
```
 
## 🔄 Workflow
 
1. **Quote Generation**
   - Scheduler triggers the quote generation process
   - Quote is fetched and stored in database
 
2. **Image Creation**
   - Quote is rendered on a custom image template
   - Image is temporarily stored on disk
 
3. **Image Upload**
   - Generated image is uploaded to hosting service
   - URL is stored in database
 
4. **Instagram Posting**
   - Image URL is retrieved from database
   - Content is posted to Instagram via Graph API
   - Image is cleaned up from temporary storage
 
## 🔐 Security Considerations
 
- API keys are stored in application properties
- Access tokens should be rotated regularly
- Temporary files are cleaned up after processing
- Database credentials should be properly secured
 
## 📝 Logging
 
The application uses SLF4J for logging with different levels:
- INFO: Regular operation logging
- ERROR: Exception and error logging
- DEBUG: Detailed process logging
 
## 🤝 Contributing
 
1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request
 
## 📄 License
 
This project is licensed under the MIT License - see the LICENSE file for details.

https://quotegenesis.up.railway.app/api/quotes/generate
https://quotegenesis.up.railway.app/api/generateImage
https://quotegenesis.up.railway.app/api/postToInstagram
