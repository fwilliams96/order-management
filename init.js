// init.js

db = db.getSiblingDB("order-management");

// Add categories
const categories = [
  { _id: UUID(), name: "Beverages", parentId: null },
  { _id: UUID(), name: "Wine", parentId: null },
  { _id: UUID(), name: "Desserts", parentId: null },
];

categories[1].parentId = categories[0]._id

db.categories.insertMany(categories);

// Add products with category references
db.products.insertMany([
  { _id: UUID(), name: "Wine Deluxe", price: 50.0, categoryId: categories[1]._id, image: { url: "https://images.pexels.com/photos/121191/pexels-photo-121191.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" } },
  { _id: UUID(), name: "Crema Catalana", price: 3.0, categoryId: categories[2]._id, image: { url: "https://images.pexels.com/photos/29655064/pexels-photo-29655064/free-photo-of-acogedor-entorno-otonal-con-bebida-caliente-y-sueter.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" } }
]);