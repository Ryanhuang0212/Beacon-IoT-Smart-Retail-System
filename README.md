# Beacon IoT Smart Retail System
This project integrates multiple modules including App, Beacon, RFID, Big Data Analytics, Image Recognition, and Firebase Cloud Database. Although some implementations differed from our original plans, we strived to retain and enhance core functionalities for better performance and user experience.

1 App Module
  Completed core features: member registration, personal info, e-wallet, and barcode display
  Includes product browsing, search, cart management, and personalized recommendations
  Supports push notifications and real-time shopping interactions

2 Beacon Module
Push Notifications:
  Utilizes Bluetooth Low Energy (BLE) to detect customer entry and push promotional content
  Each product includes floor and zone IDs for personalized messaging

Indoor Positioning:
  Tracks user location in the store using Beacon signal strength

3 RFID Checkout System
Checkout Process:
  Items are detected by RFID readers when removed from shelves; data is uploaded to Firebase
  Closing the fridge door triggers checkout, automatically calculating total and updating the cart

Purchase Records:
  After payment, transaction records are added to the user’s purchase history
  
4 Big Data Analytics
Personalized Recommendations:
  Collaborative filtering analyzes user preferences and suggests similar products
  
Storewide Promotions:
  Identifies high sales and repurchase rate products, and pushes recommendations to users within Beacon range

5 Image Recognition Module
Visual Search:
  Built with PyTorch and YOLOv4 to recognize product images and display item details
  Users can scan an item to check availability or find the most similar product — even outside the store

6 Firebase Cloud Database
  Stores product data, user profiles, and shopping history
  Enables real-time data synchronization and supports all system modules

![image](https://github.com/user-attachments/assets/eeea4142-4702-42e5-b670-4e2862d3ce4c)
