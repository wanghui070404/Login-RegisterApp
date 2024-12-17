const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const cors = require('cors');

const app = express();
const port = 3000;

// Middleware
app.use(bodyParser.json());
app.use(cors());

// Kết nối MongoDB Atlas
const mongoUri = "mongodb+srv://maixuanhuy1710:07042004@usermanager.iir2v.mongodb.net/?retryWrites=true&w=majority&appName=UserManager";
mongoose.connect(mongoUri, { useNewUrlParser: true, useUnifiedTopology: true })
    .then(() => console.log('Kết nối MongoDB Atlas thành công'))
    .catch(err => console.log('Lỗi kết nối MongoDB:', err));

// Định nghĩa schema và model MongoDB
const userSchema = new mongoose.Schema({
    username: { type: String, required: true },
    email: { type: String, unique: true, required: true },
    password: { type: String, required: true }
});

const User = mongoose.model('User', userSchema);

// Đăng ký
app.post('/register', async (req, res) => {
    try {
        const { username, email, password } = req.body;
        const hashedPassword = await bcrypt.hash(password, 10);
        const newUser = new User({ username, email, password: hashedPassword });
        await newUser.save();
        res.status(201).send('Đăng ký thành công');
    } catch (error) {
        res.status(400).send('Lỗi đăng ký: ' + error.message);
    }
});

// Đăng nhập
app.post('/login', async (req, res) => {
    try {
        const { username, password } = req.body;
        const user = await User.findOne({ username });
        if (!user) return res.status(404).send('Username không tồn tại');

        const isValid = await bcrypt.compare(password, user.password);
        if (!isValid) return res.status(401).send('Sai mật khẩu');

        const token = jwt.sign({ id: user._id }, 'secret', { expiresIn: '1h' });
        res.json({ token });
    } catch (error) {
        res.status(400).send('Lỗi đăng nhập: ' + error.message);
    }
});

// API lấy thông tin người dùng
app.get('/profile', async (req, res) => {
    try {
        const token = req.headers.authorization;
        if (!token) return res.status(401).send('Token không hợp lệ');

        const decoded = jwt.verify(token, 'secret');
        const user = await User.findById(decoded.id);
        if (!user) return res.status(404).send('Người dùng không tồn tại');

        res.json({ username: user.username, email: user.email });
    } catch (error) {
        res.status(400).send('Lỗi xác thực: ' + error.message);
    }
});

app.listen(3000, '0.0.0.0', () => {
    console.log('Server đang chạy tại http://0.0.0.0:3000');
});

