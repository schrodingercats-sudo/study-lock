

# 📱 StudyLock — Product Requirements Document (PRD)

**Version:** 1.0
**Date:** February 12, 2026
**Platform:** Android (Native — Kotlin)
**Author:** Product Team
**Status:** Draft

---

## Table of Contents

1. Executive Summary
2. Problem Statement & Vision
3. Target Audience & User Personas
4. Feature Breakdown (Detailed)
5. App Architecture & Tech Stack
6. Information Architecture & Sitemap
7. Screen-by-Screen Wireframe Descriptions
8. User Flows
9. Backend Algorithms
10. Database Schema (Supabase)
11. Firebase Configuration
12. UI/UX Design System
13. Safety & Anti-Cheat Mechanisms
14. Reward & Gamification Engine
15. Notification Strategy
16. Analytics & Reporting
17. Monetization Strategy
18. Release Phases & Roadmap
19. Risk Assessment
20. Success Metrics & KPIs

---

## 1. Executive Summary

**StudyLock** is an Android application designed to enforce disciplined study habits by locking the user's home screen and restricting phone usage to only whitelisted essential apps (WhatsApp, Phone, Messages, Google, and select AI tools like ChatGPT and Perplexity). The phone unlocks only after the user completes a Pomodoro study session followed by a knowledge test with a minimum 80% pass threshold. The app gamifies academic progress using a Hill Climb Racing-inspired reward system, supports real-time online quizzes between students, provides teacher-student interaction through doubt-solving, voice and video calls, and generates comprehensive weekly or monthly progress reports.

---

## 2. Problem Statement & Vision

### Problem

Students spend an average of 4–7 hours daily on smartphones, with the majority of that time on entertainment and social media. Existing focus apps either lock the phone entirely (frustrating) or rely on self-discipline timers that users simply dismiss. There is no app that combines enforced focus with academic accountability, gamified motivation, and a social competitive layer—all in one platform.

### Vision

To build the most effective study-enforcement app for Android that doesn't just block distractions but actively converts screen time into productive study time, validates learning through tests, rewards consistency through gamification, and builds a competitive academic community.

---

## 3. Target Audience & User Personas

### Primary Users

**Persona 1 — The Distracted Student (Age 14–22)**
This is a high school or college student who knows they should study more but constantly picks up their phone. They respond well to gamification and competition. They need an external enforcement mechanism because willpower alone isn't enough.

**Persona 2 — The Competitive Learner (Age 14–22)**
This student is already motivated but wants to track progress, compete with peers, and earn tangible rewards. They are drawn to leaderboards, badges, and social recognition.

**Persona 3 — The Concerned Parent**
A parent who wants to ensure their child is studying productively and wants visibility into phone usage versus study time. They may set up the app on their child's phone.

### Secondary Users

**Persona 4 — The Teacher/Mentor**
A teacher who wants to assign quizzes, resolve doubts, and monitor student progress remotely. They need a clean dashboard with minimal friction.

### User Demographics

The core user base consists of students aged 14–22, primarily in India and other developing markets where competitive exam culture is strong. Secondary markets include parents of students aged 10–16 and tutors or coaching institute teachers who manage batches of students.

---

## 4. Feature Breakdown (Detailed)

### 4.1 — Home Screen Lock System

**Core Concept:** When the user activates a study session, the phone's home screen and all non-whitelisted apps become inaccessible. The lock is enforced through an Android overlay service and Device Administrator API.

**Whitelisted Apps (Always Accessible):**
WhatsApp (messaging only — not Status or Channels), Phone (native dialer), Messages (SMS), Google Search, ChatGPT, Perplexity AI, Claude, and Gemini. The user may add up to 2 additional apps from a curated safe list (e.g., Calculator, Dictionary).

**Lock Behavior:** When the user presses the home button or switches apps, a full-screen overlay appears saying "You're in Study Mode. Complete your session to unlock." The overlay cannot be dismissed. Notification shade is restricted to whitelisted app notifications only. The power button does not bypass the lock (app restarts on reboot via boot receiver). Uninstall protection is enabled through Device Admin.

**Unlock Conditions:** The Pomodoro timer must have completed at least one full cycle. The post-session test must be passed with a minimum score of 80%. If the test is failed, additional lock time is added (configurable: 5, 10, or 15 minutes, defaulting to 10 minutes), after which the user can retake the test.

### 4.2 — Pomodoro Study Timer

**Timer Modes:** Classic Pomodoro consists of 25 minutes of study followed by a 5-minute break. Extended Pomodoro consists of 50 minutes of study followed by a 10-minute break. Custom Mode allows the user to define study and break durations from 10 minutes to 120 minutes. Exam Mode is a continuous session with no breaks, lasting 1–3 hours.

**Timer Features:** A circular animated progress ring is displayed on a minimal distraction-free screen. Ambient sound options are available including white noise, rain, lo-fi beats, and library ambiance. The timer runs as a foreground service and cannot be killed. Session history is logged with timestamps, duration, and subject tags. A break screen shows a motivational quote and a countdown to the next session.

**Timer End Behavior:** When the timer ends, a gentle chime plays, the screen transitions to the Test Gate screen, and the home screen remains locked until the test is passed.

### 4.3 — Daily Motivational Quotes

A motivational quote is displayed on the lock screen overlay, the break screen between Pomodoro sessions, the app home screen (refreshes every 6 hours), and in push notifications at user-configured times (default: 7 AM and 9 PM).

The quote database contains over 500 curated quotes from scientists, leaders, and educators, stored in Supabase. Quotes are categorized by theme: discipline, perseverance, intelligence, exam motivation, and failure recovery. A personalized quote engine selects quotes based on the user's recent performance. For example, after a failed test the system shows resilience quotes, and after a streak it shows ambition quotes.

### 4.4 — Test Gate (Pre-Unlock Knowledge Test)

**Test Trigger:** Automatically presented after every completed Pomodoro session before the home screen unlocks.

**Test Configuration:** The subject and topic are selected by the user before starting the session. Question count ranges from 5 to 15 questions, defaulting to 10. Question types include multiple choice (4 options), true or false, and fill-in-the-blank. There is a time limit of 30 seconds per question for MCQ and true/false, and 60 seconds for fill-in-the-blank. The passing threshold is 80% and above.

**Pass Behavior:** A success animation plays (confetti, green glow), the home screen unlocks, XP is awarded based on score and speed, and the streak counter increments.

**Fail Behavior:** A failure animation plays (Hill Climb Racing-style crash effect with screen shake, vehicle tumbling, and "Try Again" text), additional lock time is added (configurable, default 10 minutes), a hint review screen shows correct answers with explanations, the user can retake a different set of questions from the same topic after the penalty period, and consecutive failures increase penalty time by 5 minutes each (capped at 30 minutes).

**Question Source:** Teachers can upload questions via the Teacher Portal. A built-in question bank is organized by standard (grade 6–12) and subject. An AI-generated question option uses the integrated AI API to generate questions from user-provided notes or textbook photos (OCR-based).

### 4.5 — Gamification & Reward System (Hill Climb Racing Theme)

**Core Metaphor:** The student is a driver on an academic hill. Every completed task (study session, passed test, streak day) moves the vehicle up the hill. Failed tests or broken streaks cause the vehicle to tumble backward with a crash animation.

**Reward Types:**

XP (Experience Points) are earned as follows: Completed Pomodoro session awards 50 XP, a passed test awards 100 XP (with a bonus of +10 XP per percentage above 80%), a daily login awards 20 XP, a 7-day streak awards 200 XP bonus, winning an online quiz awards 300 XP, and doubt resolved (teacher) awards 50 XP.

Levels progress as follows: Level 1 (Beginner) requires 0–500 XP, Level 2 (Focused) requires 500–1500 XP, Level 3 (Scholar) requires 1500–3500 XP, Level 4 (Warrior) requires 3500–7000 XP, Level 5 (Champion) requires 7000–15000 XP, and Level 6 (Legend) requires 15000 and above.

Stickers and Badges are awarded for milestones. "First Step" is awarded for completing the first session, "On Fire" for a 7-day streak, "Quiz Master" for winning 10 online quizzes, "Night Owl" for studying past midnight, "Early Bird" for studying before 6 AM, and "Hill Conqueror" for reaching Level 5.

Voucher Rewards are distributed at level-up milestones. Level 3 earns a \$1 Google Play voucher, Level 5 earns a \$5 Amazon voucher, and "Monthly Topper" on the leaderboard earns a \$10 voucher. Vouchers are funded through the app's monetization model (see Section 17).

**Social Sharing:** On task completion or level-up, a shareable card is generated with the Hill Climb Racing-style graphic showing the vehicle at a new height. It displays "I just reached Level 4 on StudyLock! 📚🏔️" with the user's stats. It is shareable to WhatsApp, Instagram Stories, and Twitter/X. On failure, a humorous crash card can optionally be shared (user's choice).

### 4.6 — Progress Graphs & Screen Time Analytics

**Student Dashboard Graphs:**

The Daily Study Time graph is a bar chart showing hours studied per day over the last 7 and 30 days. The Test Score Trend is a line graph of test scores over time, filterable by subject. The Screen Time Breakdown is a pie chart showing time spent on the StudyLock timer, whitelisted apps, and post-unlock free time. The Streak Calendar is a GitHub-style heatmap showing daily activity intensity. The Subject Distribution is a donut chart showing percentage of study time per subject. The XP Progression is a line graph showing cumulative XP over time.

**Teacher Dashboard Graphs:**

The Class Performance Overview shows the average test scores per student in a horizontal bar chart. The Engagement Heatmap shows when students are most active by hour of day and day of week. The Quiz Results Table displays scores, time taken, and rank per quiz. The Doubt Resolution Metrics show the average response time and resolution rate.

### 4.7 — Account System

**Authentication (Firebase Auth):** Sign-up options include Email and Password, Google Sign-In, and Phone Number (OTP). Role selection occurs at sign-up with the user choosing between Student and Teacher. Student profiles include name, grade/standard (6–12 or college), school/institution name, subjects (multi-select), profile photo, and parent email (optional, for reports). Teacher profiles include name, subjects taught, institution, qualification, and verification via institution email or manual review.

**Account Pages:** The pages include Login, Sign Up, Forgot Password, Profile View and Edit, Progress Dashboard, Task Completion History, Screen Time Analytics, Settings (timer defaults, notification preferences, theme, whitelisted apps), and Logout and Delete Account.

### 4.8 — Leaderboard System

**Leaderboard Types:**

The Global Leaderboard ranks all users by XP with weekly reset for top-10 rewards. The School or Institution Leaderboard allows users from the same institution to compete. The Subject Leaderboard ranks users by test scores in a specific subject. The Friends Leaderboard allows users to add friends and compete in a private group.

**Leaderboard Display:** Each entry shows rank, profile photo, username, XP, level badge, streak count, and change indicator (up/down arrows). Anti-gaming measures are implemented so that XP from repeated identical tests is capped, and abnormal activity (100 sessions in a day) is flagged and reviewed.

### 4.9 — Online Quiz System

**Quiz Modes:**

Quick Match pairs two students randomly based on the same subject and standard for a 10-question, 5-minute quiz. Classroom Quiz is created by a teacher and assigned to a class with a scheduled start time; all students take it simultaneously. Challenge a Friend allows one user to send a quiz challenge to a specific friend.

**Quiz Flow:** The initiator selects a subject and standard, then matchmaking finds an opponent (within 30 seconds or a bot fills in). A 3-second countdown leads into simultaneous question display. Both users answer the same questions in the same order. Real-time score updates appear after each question. The final result screen shows a comparison with XP awarded. A post-quiz option to voice call or video call the opponent for discussion is available.

**Voice & Video Call:** This is powered by WebRTC with a signaling server on Supabase Edge Functions. Calls are available post-quiz with the opponent, for doubt-solving with teachers (scheduled or on-demand), and in study groups (up to 4 participants). Safety features include calls being recordable with consent for moderation, a one-tap report button for inappropriate behavior, and calls limited to 15 minutes for free-tier users.

### 4.10 — Anti-Cheat & Safety Mechanisms

**During Tests and Quizzes:**

Screen recording detection causes the test to auto-submit if screen recording or screenshots are detected. Split-screen blocking prevents the app from running in multi-window mode during tests. Tab-switch detection counts the number of times the user leaves the test screen, and more than 2 switches auto-submits the test with a flag. Question randomization ensures that question order and option order are randomized per user. A time anomaly detector flags users who answer all questions in under 2 seconds each. A copy-paste blocker disables clipboard access during tests. A camera proctor (optional, for high-stakes quizzes) uses the front camera to detect face presence and flags if the face disappears for more than 5 seconds.

**During Lock Screen:**

The Device Admin API prevents uninstallation while study mode is active. A Boot Receiver restarts the lock service if the phone is rebooted. An Accessibility Service detects attempts to force-stop the app. Safe Mode Detection alerts the user that bypassing lock mode via safe mode will reset their streak. A Time Manipulation Check compares device time with server time to prevent clock manipulation.

### 4.11 — Report Generation

**Report Types:**

Weekly Reports include total study hours, average test score, streak count, screen time breakdown, top subjects, and areas for improvement. Monthly Reports include all weekly data plus month-over-month trends, leaderboard position changes, reward summary, and teacher comments (if applicable).

**Delivery:** Reports are viewable in-app on a dedicated Reports page, downloadable as PDF, and sent via email to the user and optionally to a parent's email. Teacher reports show a per-student and class-aggregate view.

### 4.12 — Doubt Solving System

**How It Works:**

A student posts a doubt by typing text, uploading a photo (e.g., a textbook problem), or recording a voice note. The doubt is tagged with a subject and topic. It is routed to available teachers who teach that subject. Teachers can respond via text, photo, voice note, or a short video explanation. If no teacher responds in 30 minutes, the doubt is escalated to a community feed where other students can answer. A "Best Answer" marker is awarded by the student or by upvotes.

**Teacher Availability:** Teachers set their available hours in their profile. A green dot indicator shows online/available status. Students can request a live call for complex doubts.

---

## 5. App Architecture & Tech Stack

### Frontend

The platform is Android Native built with Kotlin and Jetpack Compose for the UI framework. Navigation is handled by the Navigation Component. Dependency injection uses Hilt. Async operations are managed with Kotlin Coroutines and Flow. Image loading uses Coil. The minimum SDK is API 26 (Android 8.0), targeting SDK API 34.

### Backend Services

Authentication uses Firebase Auth (Email, Google, Phone). Realtime Database uses Firebase Realtime Database for presence and live quiz sync. Cloud Messaging uses Firebase Cloud Messaging (FCM) for push notifications. The primary database is Supabase (PostgreSQL) for all structured data. File storage uses Supabase Storage for profile photos, doubt images, and report PDFs. Edge Functions use Supabase Edge Functions (Deno) for matchmaking, report generation, and anti-cheat validation. Real-time features use Supabase Realtime for leaderboard updates, doubt-solving chat, and notifications. Voice and video calling uses WebRTC with Supabase Edge Functions as the signaling server.

### Third-Party Integrations

OCR for notes uses Google ML Kit On-Device Text Recognition. AI question generation uses the OpenAI API (GPT-4) or Anthropic Claude API. PDF generation for reports uses iText for Android. Analytics use Mixpanel or Amplitude. Crash reporting uses Firebase Crashlytics.

---

## 6. Information Architecture & Sitemap

```
StudyLock App
│
├── Onboarding
│   ├── Welcome Screens (3 slides)
│   ├── Role Selection (Student / Teacher)
│   └── Permission Grants (Overlay, Device Admin, Notifications)
│
├── Auth
│   ├── Login
│   ├── Sign Up
│   └── Forgot Password
│
├── Student Flow
│   ├── Home Dashboard
│   │   ├── Quick Start Study Session
│   │   ├── Today's Stats (study time, tests, streak)
│   │   ├── Motivational Quote Card
│   │   └── Upcoming Quizzes
│   │
│   ├── Study Mode
│   │   ├── Subject & Topic Selection
│   │   ├── Timer Configuration
│   │   ├── Active Timer Screen
│   │   ├── Break Screen
│   │   └── Test Gate
│   │       ├── Test Screen
│   │       ├── Result — Pass (Unlock)
│   │       └── Result — Fail (Extended Lock)
│   │
│   ├── Quizzes
│   │   ├── Quick Match
│   │   ├── Classroom Quizzes
│   │   ├── Challenge a Friend
│   │   └── Quiz History
│   │
│   ├── Leaderboard
│   │   ├── Global
│   │   ├── School
│   │   ├── Subject
│   │   └── Friends
│   │
│   ├── Doubts
│   │   ├── Post a Doubt
│   │   ├── My Doubts
│   │   ├── Community Feed
│   │   └── Chat / Call with Teacher
│   │
│   ├── Progress
│   │   ├── Graphs & Analytics
│   │   ├── Screen Time
│   │   ├── Reports (Weekly / Monthly)
│   │   └── Rewards & Badges
│   │
│   ├── Profile
│   │   ├── View / Edit Profile
│   │   ├── Settings
│   │   └── Logout / Delete Account
│   │
│   └── Rewards Store
│       ├── Available Vouchers
│       ├── Sticker Collection
│       └── Redemption History
│
├── Teacher Flow
│   ├── Home Dashboard
│   │   ├── Active Students
│   │   ├── Pending Doubts
│   │   └── Upcoming Quizzes
│   │
│   ├── Quiz Management
│   │   ├── Create Quiz
│   │   ├── Question Bank
│   │   └── Quiz Results
│   │
│   ├── Doubt Resolution
│   │   ├── Incoming Doubts
│   │   ├── Resolved Doubts
│   │   └── Call Schedule
│   │
│   ├── Student Analytics
│   │   ├── Individual Student View
│   │   └── Class Overview
│   │
│   └── Profile & Settings
│
└── Lock Screen Overlay
    ├── "Study Mode Active" Message
    ├── Timer Remaining
    ├── Motivational Quote
    └── Emergency Unlock (Password — set by parent/teacher)
```

---

## 7. Screen-by-Screen Wireframe Descriptions

### 7.1 — Onboarding Screens (3 slides)

**Slide 1 — "Lock Your Distractions"**
The illustration shows a phone with a lock icon and app icons fading out. The headline reads "Lock Your Distractions" and the subtext says "StudyLock blocks non-essential apps while you study. Only the tools you need stay accessible." A "Next" button and dot indicators appear at the bottom.

**Slide 2 — "Study, Test, Unlock"**
The illustration shows a Pomodoro timer transitioning into a quiz, then into an unlocked phone. The headline reads "Study, Test, Unlock" and the subtext says "Complete your study session, pass a quick test, and earn your phone time back." A "Next" button appears at the bottom.

**Slide 3 — "Climb the Hill"**
The illustration shows a Hill Climb Racing-style vehicle going up a mountain with badges and vouchers along the path. The headline reads "Climb the Hill" and the subtext says "Compete with friends, earn rewards, and track your growth. Every session takes you higher." A "Get Started" button appears at the bottom.

### 7.2 — Role Selection Screen

A clean screen with two large cards side by side. The left card shows a Student icon (graduation cap) with the label "I'm a Student." The right card shows a Teacher icon (chalkboard) with the label "I'm a Teacher." Tapping a card highlights it with the primary color and enables the "Continue" button below.

### 7.3 — Permission Grant Screen

A checklist-style screen with three permission rows. Row 1 says "Display Over Other Apps" with an explanation "(Required to lock your screen)" and a "Grant" button. Row 2 says "Device Administrator" with an explanation "(Required to prevent bypass)" and a "Grant" button. Row 3 says "Notifications" with an explanation "(Required for reminders and quotes)" and a "Grant" button. All three must be granted to proceed. A "Continue" button is grayed out until all permissions are active.

### 7.4 — Login Screen

The top section shows the StudyLock logo and tagline "Study Smart. Stay Locked." Input fields include email and password with a show/hide toggle. Below the fields are a "Login" button (primary, full-width), a "Forgot Password?" text link, a divider line with "or," a "Sign in with Google" button with the Google icon, a "Sign in with Phone" button with the phone icon, and at the bottom "Don't have an account? Sign Up" as a text link.

### 7.5 — Sign Up Screen

This mirrors the login layout with additional fields for full name, role (Student/Teacher — pre-filled from role selection), grade/standard (dropdown, for students), school/institution name, and subjects (multi-select chips). A terms and conditions checkbox and a "Create Account" button are at the bottom.

### 7.6 — Student Home Dashboard

**Top Bar:** Profile photo (circular, tappable) on the left, "Good Morning, [Name]" greeting, and a notification bell icon on the right.

**Streak Banner:** A horizontal card showing a fire icon, "🔥 12 Day Streak" with a progress bar to the next streak milestone.

**Quick Start Card:** A large prominent card with a play icon, "Start Study Session" text, and the last-used subject and timer config shown as a subtitle. Tapping launches the study mode flow.

**Today's Stats Row:** Three small metric cards in a horizontal row showing "2.5h Studied" with a book icon, "3 Tests Passed" with a checkmark icon, and "450 XP Earned" with a star icon.

**Motivational Quote Card:** A styled card with a quote, author attribution, and a subtle gradient background matching the theme.

**Upcoming Quizzes Section:** A horizontal scrollable list of quiz cards showing the quiz name, subject, time, and a "Join" button.

**Bottom Navigation Bar:** Five tabs consisting of Home (house icon), Quizzes (trophy icon), Doubts (question mark icon), Leaderboard (podium icon), and Profile (person icon).

### 7.7 — Study Mode — Subject & Topic Selection

**Subject Grid:** A grid of subject cards (Mathematics, Physics, Chemistry, Biology, English, History, etc.) each with a distinct icon and color. Tapping a subject expands to show a list of topics within that subject (e.g., Mathematics → Algebra, Calculus, Geometry). The user taps a topic to select it, and a checkmark appears.

**Timer Config Section (Below):** This shows radio buttons for timer mode (Classic 25/5, Extended 50/10, Custom, Exam). For Custom mode, two sliders appear for study and break duration. A "Number of cycles" dropdown offers options from 1 to 8. A "Start Session" button is large and prominent with a lock icon.

### 7.8 — Active Timer Screen

**Full-Screen Minimal Design:** A dark background with a large circular timer ring in the center. The ring fills clockwise as time progresses. The center of the ring shows the remaining time in large font (e.g., "18:42") with the label "Focus Time" below it. The subject and topic are displayed above the ring in smaller text. A "Sounds" button in the bottom-left opens the ambient sound selector. A "Pause" button in the bottom-right pauses the timer (limited to 2 pauses per session, each for a maximum of 2 minutes). No navigation is possible since the bottom bar is hidden.

### 7.9 — Break Screen

A relaxing color scheme with a softer background is used. The header says "Great work! Take a break." A motivational quote is displayed in the center. A countdown to the next session appears at the bottom (e.g., "Next session in 4:32"). Tips are displayed like "Stretch, hydrate, look away from the screen." An auto-transition begins the next session when the break ends.

### 7.10 — Test Gate Screen

**Pre-Test:** A card shows "Time to prove what you learned!" with the subject and topic, number of questions, the time limit, and the pass threshold of 80%. A "Start Test" button appears.

**Test Screen:** A progress bar across the top shows "Question 3 of 10." The question text is displayed prominently. For MCQ questions, four option buttons are arranged vertically, and the selected option highlights in the primary color. A per-question timer is shown in the top-right. "Previous" and "Next" buttons appear at the bottom (Previous is disabled on the first question). An anti-cheat overlay warning appears if the user switches apps.

**Result — Pass:** A full-screen confetti animation plays. A large checkmark icon appears with the text "You Passed! 90%." The details show "9/10 correct • +190 XP." A "View Answers" button and an "Unlock Phone" button (primary, large) are displayed. Tapping "Unlock Phone" removes the overlay lock.

**Result — Fail:** A Hill Climb Racing crash animation plays with the vehicle tumbling down a hill with comic effects. The text "Not quite! 60%" appears. Details show "6/10 correct • 80% needed." A note says "Phone locked for 10 more minutes." A "Review Answers" button reveals correct answers with explanations. A countdown timer shows when the retake becomes available. An "I'll try again" button is grayed out until the penalty expires.

### 7.11 — Quiz Lobby (Quick Match)

The header says "Quick Match." A subject selector shows a horizontal scrollable chip list. A standard or grade selector appears as a dropdown. A "Find Opponent" button is large with a search icon. An animated matchmaking screen shows two profile card slots with a "VS" between them. The user's card is filled; the opponent's card shows a searching animation. When an opponent is found, their profile loads in, a 3-second countdown appears, and the quiz begins.

### 7.12 — Live Quiz Screen

A split layout is used. The top section shows both players' names, photos, and current scores. The question area is in the middle with the same format as the Test Gate. When a player answers, their score updates in real-time. A correct answer flashes green and an incorrect answer flashes red. After the last question, a final comparison screen appears with "You Win!" or "You Lose!" along with XP earned. Post-quiz action buttons offer "Call Opponent" and "Rematch."

### 7.13 — Leaderboard Screen

Tabs across the top show Global, School, Subject, and Friends. The top 3 users are displayed prominently as a podium graphic with the first-place user elevated in the center, flanked by second and third place, showing profile photos, names, and XP. Below the podium is a scrollable list of ranks starting from the fourth position. Each row shows the rank number, a change indicator (green arrow up or red arrow down), the profile photo, the name, XP, level badge, and streak count. The user's own rank is pinned at the bottom of the screen with a highlighted card regardless of their position.

### 7.14 — Doubt Posting Screen

A top section reads "Ask a Doubt." An input field allows text entry with a placeholder "Type your question..." An attachment row offers buttons for Camera (take a photo of a textbook), Gallery (upload an image), and Voice (record a voice note). Tag selectors allow choosing the subject and topic from dropdowns. A priority toggle has options for Normal and Urgent (Urgent shows to teachers first). A "Post Doubt" button submits the doubt.

### 7.15 — Doubt Feed / Chat

The My Doubts tab shows a list of the user's posted doubts with their status (Pending, Answered, Resolved). Tapping a doubt opens a chat-style thread. The Community Feed tab shows all public doubts from students in the same standard. Teacher's View shows a list of incoming doubts filtered by subject, with an "Accept" button to start resolving.

The chat thread uses a messaging-style UI. Student messages appear on the right, and teacher/peer responses appear on the left. Messages support text, images, and voice notes. Action buttons at the bottom offer "Mark as Resolved," "Request Call," and "Thank Teacher" (awards the teacher XP).

### 7.16 — Progress & Analytics Screen

Tabs across the top show Study, Tests, Screen Time, and Rewards.

The Study Tab displays the daily study hours bar chart (7-day and 30-day toggle), subject distribution donut chart, streak calendar heatmap, and total hours studied to date.

The Tests Tab displays the score trend line graph (filterable by subject), average score card, best score card, total tests taken, and pass/fail ratio.

The Screen Time Tab displays the daily screen time bar chart (study time vs. free time vs. whitelisted apps), an app usage breakdown list, and a comparison of this week versus last week.

The Rewards Tab displays total XP and current level, a badge collection grid (earned badges are colorful, locked badges are grayed out), a sticker collection, and a voucher redemption history.

### 7.17 — Report Screen

The header says "My Reports." A toggle switches between Weekly and Monthly. A list of generated reports appears with the date range, a summary line (e.g., "12h studied, 85% avg score"), and a "View" button. Tapping "View" opens a detailed in-app report. A "Download PDF" button appears at the bottom. A "Send to Parent" button emails the report to the registered parent email.

### 7.18 — Teacher Home Dashboard

The top bar shows "Teacher Dashboard" with a notification bell. Stat cards display "24 Active Students," "8 Pending Doubts," and "3 Quizzes This Week." A section titled "Recent Doubts" shows the latest 3 doubts with a "View All" link. A section titled "Upcoming Quizzes" lists scheduled quizzes with options to "Edit" or "View Results." A section titled "Top Students" shows the top 5 students by XP with a "View Leaderboard" link.

### 7.19 — Teacher Quiz Creation

A step-by-step form is used. Step 1 covers Quiz Details including the quiz name, subject, standard/grade, scheduled date and time, and duration. Step 2 covers Questions where the teacher can add questions manually (type the question, add options, mark the correct answer, and add an explanation), import from the question bank (search and filter by topic, then select questions), or use AI generate (enter a topic, and the system generates 10 questions for the teacher to review and edit). Step 3 shows a Review and Publish preview with a "Publish" button. Published quizzes send a push notification to all students in the assigned class.

### 7.20 — Settings Screen

Sections include Account (Edit Profile, Change Password, Linked Accounts), Study Preferences (Default Timer Mode, Default Break Duration, Subjects), Lock Screen (Whitelisted Apps selection with toggles, Emergency Unlock Password, Lock Strictness with options for Normal and Strict where Strict disables the pause and increases anti-cheat measures), Notifications (Motivational Quotes toggle and time selector, Session Reminders, Quiz Notifications, and Report Notifications), Appearance (Theme selection for Light, Dark, or System Default; Accent Color selection), Reports (Frequency toggle for Weekly or Monthly, Parent Email for report delivery), and a Danger Zone (Logout button and Delete Account button with confirmation).

### 7.21 — Lock Screen Overlay

A full-screen overlay (TYPE_APPLICATION_OVERLAY) uses a semi-transparent dark background. Centered content shows the StudyLock logo, "Study Mode Active" as the heading, the time remaining (e.g., "18:42 remaining"), a motivational quote that rotates every 60 seconds, and "Complete your session to unlock" as the subtext. An emergency unlock area at the bottom allows the user to "Tap 5 times to enter emergency password." The emergency unlock logs the event and breaks the streak.

---

## 8. User Flows

### 8.1 — First-Time User Flow

The user downloads the app from the Play Store, then opens the app and sees the three onboarding slides. They select their role (Student or Teacher) and grant required permissions (Overlay, Device Admin, Notifications). They are directed to the Sign-Up screen where they fill in their details. They select subjects and grade. They reach the Home Dashboard and see an interactive tutorial overlay highlighting "Start your first session." They tap "Start Study Session" and are guided through their first Pomodoro and test experience.

### 8.2 — Daily Study Session Flow

The student opens the app and sees the Home Dashboard with their streak, today's stats, and a motivational quote. They tap "Start Study Session" and select the subject and topic then configure the timer. They tap "Start" and the home screen locks immediately. The timer runs as a foreground service. On completion, the Test Gate appears. They take the test. If they pass (80% or above), the home screen unlocks with a celebration and XP is awarded. If they fail, additional lock time is added, they review incorrect answers, wait for the penalty to expire, retake, and repeat until they pass.

### 8.3 — Online Quiz Flow

The student navigates to the Quizzes tab. They select "Quick Match" and choose a subject and standard. Matchmaking begins and finds an opponent. A 3-second countdown starts the quiz. Both students answer the same questions simultaneously with real-time score updates. After the last question, results are compared and XP is awarded. The student can optionally call the opponent for discussion or request a rematch.

### 8.4 — Doubt Resolution Flow

The student navigates to the Doubts tab and taps "Post a Doubt." They type or photograph their question, tag the subject and topic, and submit. The doubt is routed to available teachers for that subject. A teacher receives a push notification, opens the doubt, and responds via text, image, or voice. The student is notified of the response. A conversation thread continues until the student marks the doubt as "Resolved." The teacher earns XP for the resolution.

### 8.5 — Teacher Quiz Flow

The teacher navigates to the Quiz Management section, taps "Create Quiz," fills in the details (name, subject, class, schedule), adds questions (manually, from the bank, or via AI generation), reviews and publishes the quiz. Students receive a push notification. At the scheduled time, students join the quiz. After completion, the teacher views results and analytics on the Quiz Results page.

### 8.6 — Report Generation Flow

The system automatically generates a report every Sunday (weekly) or on the first of the month (monthly) based on the user's preference. A push notification alerts the student that the report is ready. The student opens the Reports page, views the report, and can download the PDF. If a parent email is configured, the report is automatically emailed to the parent as well.

---

## 9. Backend Algorithms

### 9.1 — Matchmaking Algorithm (Online Quiz)

```
FUNCTION findMatch(student):
    pool = getActiveMatchmakingPool()
    
    // Filter by same subject AND same standard
    candidates = pool.filter(
        s => s.subject == student.subject 
        AND s.standard == student.standard 
        AND s.id != student.id
    )
    
    // Sort by closest XP (for fair matching)
    candidates.sortBy(s => ABS(s.xp - student.xp))
    
    IF candidates.length > 0:
        opponent = candidates[0]
        removeFromPool(opponent)
        removeFromPool(student)
        createQuizSession(student, opponent)
        RETURN opponent
    ELSE:
        // Wait up to 30 seconds
        waitWithTimeout(30 seconds):
            ON new candidate matching criteria:
                RETURN candidate
            ON timeout:
                // Create bot opponent with similar XP
                bot = createBotOpponent(student.xp, student.subject)
                createQuizSession(student, bot)
                RETURN bot
```

### 9.2 — Test Question Selection Algorithm

```
FUNCTION selectQuestions(subject, topic, count, studentHistory):
    allQuestions = getQuestionBank(subject, topic)
    
    // Remove questions the student answered correctly 
    // in the last 3 sessions (avoid repetition)
    recentCorrect = studentHistory.last3Sessions.correctQuestionIds
    available = allQuestions.filter(q => q.id NOT IN recentCorrect)
    
    // Categorize by difficulty
    easy = available.filter(q => q.difficulty == "easy")
    medium = available.filter(q => q.difficulty == "medium")
    hard = available.filter(q => q.difficulty == "hard")
    
    // Adaptive difficulty based on recent performance
    recentAvgScore = studentHistory.last5Tests.averageScore
    
    IF recentAvgScore >= 90:
        distribution = {easy: 10%, medium: 40%, hard: 50%}
    ELSE IF recentAvgScore >= 70:
        distribution = {easy: 20%, medium: 50%, hard: 30%}
    ELSE:
        distribution = {easy: 40%, medium: 40%, hard: 20%}
    
    selected = []
    FOR EACH difficulty, percentage IN distribution:
        n = ROUND(count * percentage)
        selected.ADD(randomSample(difficultyPool, n))
    
    // Shuffle order and option order
    SHUFFLE(selected)
    FOR EACH question IN selected:
        SHUFFLE(question.options)
    
    RETURN selected
```

### 9.3 — XP & Reward Calculation Algorithm

```
FUNCTION calculateSessionReward(session, testResult):
    xp = 0
    rewards = []
    
    // Base XP for completed session
    xp += 50
    
    // Test bonus
    IF testResult.score >= 80:
        xp += 100
        bonusPercentAbove80 = testResult.score - 80
        xp += bonusPercentAbove80 * 10
    
    // Streak bonus
    currentStreak = getUserStreak(session.userId)
    IF currentStreak > 0 AND currentStreak % 7 == 0:
        xp += 200  // Weekly streak bonus
        rewards.ADD("streak_badge_" + currentStreak)
    
    // Time-of-day bonus
    hour = session.completedAt.hour
    IF hour < 6:
        xp += 30  // Early Bird bonus
    ELSE IF hour >= 23:
        xp += 20  // Night Owl bonus
    
    // Difficulty multiplier
    IF session.timerDuration >= 50:
        xp = xp * 1.5  // Extended session bonus
    
    // Update user XP
    newTotalXP = updateUserXP(session.userId, xp)
    
    // Check level up
    newLevel = calculateLevel(newTotalXP)
    oldLevel = getUserLevel(session.userId)
    IF newLevel > oldLevel:
        rewards.ADD("level_up_" + newLevel)
        rewards.ADD(getLevelReward(newLevel))  // Voucher, etc.
        updateUserLevel(session.userId, newLevel)
    
    // Check badge milestones
    badges = checkBadgeMilestones(session.userId)
    rewards.ADD(badges)
    
    RETURN {xp: xp, rewards: rewards, newLevel: newLevel}
```

### 9.4 — Anti-Cheat Scoring Algorithm

```
FUNCTION validateTestIntegrity(testSession):
    flags = []
    integrityScore = 100  // Start at 100, deduct for violations
    
    // Check 1: Time anomalies
    FOR EACH answer IN testSession.answers:
        IF answer.timeToAnswer < 2 seconds:
            flags.ADD("SPEED_ANOMALY: Q" + answer.questionNumber)
            integrityScore -= 15
    
    // Check 2: App switches
    IF testSession.appSwitchCount > 2:
        flags.ADD("EXCESSIVE_APP_SWITCHES: " + testSession.appSwitchCount)
        integrityScore -= (testSession.appSwitchCount - 2) * 10
    
    // Check 3: Screen recording detected
    IF testSession.screenRecordingDetected:
        flags.ADD("SCREEN_RECORDING_DETECTED")
        integrityScore -= 50
    
    // Check 4: Time manipulation
    serverTime = getServerTime()
    deviceTime = testSession.deviceTimestamp
    IF ABS(serverTime - deviceTime) > 60 seconds:
        flags.ADD("TIME_MANIPULATION_SUSPECTED")
        integrityScore -= 30
    
    // Check 5: Answer pattern analysis
    IF allAnswersSameOption(testSession.answers):
        flags.ADD("UNIFORM_ANSWER_PATTERN")
        integrityScore -= 20
    
    // Determine validity
    IF integrityScore < 50:
        testSession.status = "FLAGGED_INVALID"
        testSession.xpAwarded = 0
        notifyAdmin(testSession, flags)
    ELSE IF integrityScore < 80:
        testSession.status = "FLAGGED_REVIEW"
        testSession.xpAwarded = testSession.xpAwarded * 0.5
    ELSE:
        testSession.status = "VALID"
    
    testSession.integrityScore = integrityScore
    testSession.flags = flags
    RETURN testSession
```

### 9.5 — Doubt Routing Algorithm

```
FUNCTION routeDoubt(doubt):
    // Find teachers matching subject
    teachers = getTeachers(doubt.subject)
    
    // Filter by availability
    now = getCurrentTime()
    availableTeachers = teachers.filter(t => 
        t.availableHours.includes(now.hour) 
        AND t.status == "online"
    )
    
    // Sort by response quality metrics
    availableTeachers.sortBy(t => (
        t.avgResponseTime * 0.3          // Lower is better
        + (1 - t.resolutionRate) * 0.3   // Higher rate is better
        + t.currentDoubtQueue * 0.2       // Fewer queued is better
        + (1 - t.avgRating / 5) * 0.2    // Higher rating is better
    ))
    
    // Assign to top 3 teachers (first to respond gets it)
    topTeachers = availableTeachers.slice(0, 3)
    FOR EACH teacher IN topTeachers:
        sendNotification(teacher, doubt)
    
    // Set escalation timer
    setTimeout(30 minutes):
        IF doubt.status == "PENDING":
            // Escalate to community
            doubt.visibility = "PUBLIC"
            notifyRelevantStudents(doubt)
    
    RETURN {assignedTeachers: topTeachers, escalationTime: 30 min}
```

### 9.6 — Report Generation Algorithm

```
FUNCTION generateReport(userId, period):
    // period = "weekly" or "monthly"
    startDate = calculateStartDate(period)
    endDate = now()
    
    // Gather data
    sessions = getStudySessions(userId, startDate, endDate)
    tests = getTestResults(userId, startDate, endDate)
    screenTime = getScreenTimeData(userId, startDate, endDate)
    quizzes = getQuizResults(userId, startDate, endDate)
    doubts = getDoubtActivity(userId, startDate, endDate)
    
    report = {
        summary: {
            totalStudyHours: SUM(sessions.duration) / 60,
            totalSessions: sessions.count,
            averageTestScore: AVG(tests.score),
            bestTestScore: MAX(tests.score),
            testsCompleted: tests.count,
            passRate: tests.filter(t => t.passed).count / tests.count,
            currentStreak: getCurrentStreak(userId),
            longestStreak: getLongestStreak(userId, startDate, endDate),
            xpEarned: SUM(sessions.xp + tests.xp + quizzes.xp),
            leaderboardPosition: getLeaderboardRank(userId)
        },
        
        studyBreakdown: {
            bySubject: GROUP_BY(sessions, "subject")
                .MAP(group => {subject, totalHours, avgScore}),
            byDay: GROUP_BY(sessions, "date")
                .MAP(day => {date, hours, sessions}),
            peakHours: MODE(sessions.MAP(s => s.startTime.hour))
        },
        
        screenTime: {
            totalScreenTime: SUM(screenTime.duration),
            studyTimeRatio: studyTime / totalScreenTime,
            appBreakdown: GROUP_BY(screenTime, "app")
                .MAP(app => {name, duration, percentage}),
            comparisonToPreviousPeriod: calculateDelta(previous, current)
        },
        
        improvements: generateImprovementSuggestions(tests, sessions),
        
        achievements: getNewBadges(userId, startDate, endDate),
        
        teacherComments: getTeacherComments(userId, startDate, endDate)
    }
    
    // Generate PDF
    pdf = renderReportPDF(report)
    uploadToStorage(pdf, userId)
    
    // Send email if parent email configured
    parentEmail = getParentEmail(userId)
    IF parentEmail:
        sendEmail(parentEmail, report, pdf)
    
    // Send push notification
    sendNotification(userId, "Your " + period + " report is ready!")
    
    RETURN report
```

### 9.7 — Hill Climb Progress Algorithm

```
FUNCTION updateHillPosition(userId, event):
    currentPosition = getUserHillPosition(userId)
    
    SWITCH event.type:
        CASE "session_completed":
            advancement = 10  // Base hill advancement
            currentPosition += advancement
            animation = "DRIVE_FORWARD"
            
        CASE "test_passed":
            advancement = 5 + (event.score - 80) * 0.5
            currentPosition += advancement
            animation = "DRIVE_UPHILL"
            
        CASE "test_failed":
            regression = 8
            currentPosition = MAX(0, currentPosition - regression)
            animation = "CRASH_TUMBLE"
            
        CASE "streak_broken":
            regression = 15
            currentPosition = MAX(0, currentPosition - regression)
            animation = "BIG_CRASH"
            
        CASE "quiz_won":
            advancement = 20
            currentPosition += advancement
            animation = "BOOST_JUMP"
            
        CASE "level_up":
            // Reach a new hill/terrain
            animation = "NEW_TERRAIN_UNLOCK"
    
    // Check milestones
    milestones = [100, 250, 500, 1000, 2500, 5000]
    FOR EACH milestone IN milestones:
        IF currentPosition >= milestone 
           AND previousPosition < milestone:
            triggerMilestoneReward(userId, milestone)
    
    saveHillPosition(userId, currentPosition)
    RETURN {position: currentPosition, animation: animation}
```

---

## 10. Database Schema (Supabase — PostgreSQL)

### Users Table

```
users
├── id (UUID, PK)
├── firebase_uid (VARCHAR, UNIQUE) — links to Firebase Auth
├── email (VARCHAR, UNIQUE)
├── full_name (VARCHAR)
├── role (ENUM: 'student', 'teacher')
├── profile_photo_url (TEXT)
├── grade (INTEGER, nullable) — for students
├── institution_name (VARCHAR)
├── subjects (TEXT[]) — array of subjects
├── parent_email (VARCHAR, nullable)
├── xp (INTEGER, DEFAULT 0)
├── level (INTEGER, DEFAULT 1)
├── current_streak (INTEGER, DEFAULT 0)
├── longest_streak (INTEGER, DEFAULT 0)
├── hill_position (FLOAT, DEFAULT 0)
├── last_active_at (TIMESTAMP)
├── report_frequency (ENUM: 'weekly', 'monthly', DEFAULT 'weekly')
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

### Study Sessions Table

```
study_sessions
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── subject (VARCHAR)
├── topic (VARCHAR)
├── timer_mode (ENUM: 'classic', 'extended', 'custom', 'exam')
├── study_duration_minutes (INTEGER)
├── break_duration_minutes (INTEGER)
├── cycles_completed (INTEGER)
├── started_at (TIMESTAMP)
├── completed_at (TIMESTAMP)
├── status (ENUM: 'completed', 'abandoned', 'in_progress')
├── ambient_sound (VARCHAR, nullable)
└── created_at (TIMESTAMP)
```

### Tests Table

```
tests
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── session_id (UUID, FK → study_sessions)
├── subject (VARCHAR)
├── topic (VARCHAR)
├── question_count (INTEGER)
├── correct_answers (INTEGER)
├── score_percentage (FLOAT)
├── passed (BOOLEAN)
├── time_taken_seconds (INTEGER)
├── integrity_score (INTEGER)
├── integrity_flags (JSONB)
├── xp_awarded (INTEGER)
├── attempt_number (INTEGER) — for retakes
└── completed_at (TIMESTAMP)
```

### Test Questions Table

```
test_questions
├── id (UUID, PK)
├── test_id (UUID, FK → tests)
├── question_id (UUID, FK → question_bank)
├── user_answer (VARCHAR)
├── correct_answer (VARCHAR)
├── is_correct (BOOLEAN)
└── time_to_answer_seconds (FLOAT)
```

### Question Bank Table

```
question_bank
├── id (UUID, PK)
├── subject (VARCHAR)
├── topic (VARCHAR)
├── grade (INTEGER)
├── question_text (TEXT)
├── question_type (ENUM: 'mcq', 'true_false', 'fill_blank')
├── options (JSONB) — for MCQ: [{text, isCorrect}]
├── correct_answer (VARCHAR)
├── explanation (TEXT)
├── difficulty (ENUM: 'easy', 'medium', 'hard')
├── created_by (UUID, FK → users, nullable) — teacher who created
├── is_ai_generated (BOOLEAN, DEFAULT false)
├── times_used (INTEGER, DEFAULT 0)
├── correct_rate (FLOAT) — percentage of students who got it right
└── created_at (TIMESTAMP)
```

### Quizzes Table

```
quizzes
├── id (UUID, PK)
├── quiz_type (ENUM: 'quick_match', 'classroom', 'challenge')
├── subject (VARCHAR)
├── grade (INTEGER)
├── created_by (UUID, FK → users)
├── title (VARCHAR, nullable)
├── question_ids (UUID[])
├── duration_minutes (INTEGER)
├── scheduled_at (TIMESTAMP, nullable) — for classroom quizzes
├── status (ENUM: 'pending', 'active', 'completed')
├── max_participants (INTEGER)
└── created_at (TIMESTAMP)
```

### Quiz Participants Table

```
quiz_participants
├── id (UUID, PK)
├── quiz_id (UUID, FK → quizzes)
├── user_id (UUID, FK → users)
├── score (INTEGER)
├── correct_answers (INTEGER)
├── time_taken_seconds (INTEGER)
├── rank (INTEGER)
├── xp_awarded (INTEGER)
├── answers (JSONB) — [{questionId, answer, isCorrect, timeTaken}]
└── completed_at (TIMESTAMP)
```

### Doubts Table

```
doubts
├── id (UUID, PK)
├── student_id (UUID, FK → users)
├── subject (VARCHAR)
├── topic (VARCHAR)
├── question_text (TEXT)
├── image_url (TEXT, nullable)
├── voice_note_url (TEXT, nullable)
├── priority (ENUM: 'normal', 'urgent')
├── visibility (ENUM: 'private', 'public')
├── status (ENUM: 'pending', 'assigned', 'answered', 'resolved')
├── assigned_teacher_id (UUID, FK → users, nullable)
├── resolved_at (TIMESTAMP, nullable)
└── created_at (TIMESTAMP)
```

### Doubt Messages Table

```
doubt_messages
├── id (UUID, PK)
├── doubt_id (UUID, FK → doubts)
├── sender_id (UUID, FK → users)
├── message_text (TEXT, nullable)
├── image_url (TEXT, nullable)
├── voice_note_url (TEXT, nullable)
├── is_best_answer (BOOLEAN, DEFAULT false)
└── created_at (TIMESTAMP)
```

### Rewards Table

```
rewards
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── reward_type (ENUM: 'badge', 'sticker', 'voucher', 'xp_bonus')
├── reward_name (VARCHAR)
├── reward_description (TEXT)
├── reward_image_url (TEXT)
├── voucher_code (VARCHAR, nullable)
├── voucher_value (DECIMAL, nullable)
├── is_redeemed (BOOLEAN, DEFAULT false)
├── earned_at (TIMESTAMP)
└── redeemed_at (TIMESTAMP, nullable)
```

### Screen Time Table

```
screen_time
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── date (DATE)
├── total_screen_time_minutes (INTEGER)
├── study_time_minutes (INTEGER)
├── free_time_minutes (INTEGER)
├── app_breakdown (JSONB) — [{appName, durationMinutes}]
└── created_at (TIMESTAMP)
```

### Reports Table

```
reports
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── report_type (ENUM: 'weekly', 'monthly')
├── period_start (DATE)
├── period_end (DATE)
├── report_data (JSONB) — full report content
├── pdf_url (TEXT, nullable)
├── emailed_to_parent (BOOLEAN, DEFAULT false)
├── teacher_comments (TEXT, nullable)
└── generated_at (TIMESTAMP)
```

### Leaderboard Table (Materialized / Cached)

```
leaderboard_cache
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── scope (ENUM: 'global', 'school', 'subject', 'friends')
├── scope_value (VARCHAR, nullable) — school name or subject
├── rank (INTEGER)
├── xp (INTEGER)
├── level (INTEGER)
├── streak (INTEGER)
├── previous_rank (INTEGER)
└── updated_at (TIMESTAMP)
```

### Friendships Table

```
friendships
├── id (UUID, PK)
├── user_id (UUID, FK → users)
├── friend_id (UUID, FK → users)
├── status (ENUM: 'pending', 'accepted', 'blocked')
├── created_at (TIMESTAMP)
└── accepted_at (TIMESTAMP, nullable)
```

### Motivational Quotes Table

```
quotes
├── id (UUID, PK)
├── text (TEXT)
├── author (VARCHAR)
├── category (ENUM: 'discipline', 'perseverance', 'intelligence', 
│              'exam_motivation', 'failure_recovery', 'ambition')
└── created_at (TIMESTAMP)
```

### Teacher Availability Table

```
teacher_availability
├── id (UUID, PK)
├── teacher_id (UUID, FK → users)
├── day_of_week (INTEGER) — 0=Sunday, 6=Saturday
├── start_hour (INTEGER) — 0-23
├── end_hour (INTEGER) — 0-23
└── is_active (BOOLEAN, DEFAULT true)
```

### Call Logs Table

```
call_logs
├── id (UUID, PK)
├── caller_id (UUID, FK → users)
├── callee_id (UUID, FK → users)
├── call_type (ENUM: 'voice', 'video')
├── context (ENUM: 'doubt', 'quiz', 'study_group')
├── context_id (UUID, nullable) — doubt_id or quiz_id
├── duration_seconds (INTEGER)
├── started_at (TIMESTAMP)
└── ended_at (TIMESTAMP)
```

---

## 11. Firebase Configuration

### Firebase Auth

Enabled sign-in providers include Email/Password, Google, and Phone (OTP). Custom claims are used for the `role` field ("student" or "teacher") which is set at sign-up via a Cloud Function. The Firebase UID is stored in the Supabase `users` table as `firebase_uid` to link authentication with the database.

### Firebase Realtime Database

This is used for three specific use cases. Presence tracking stores the user's online status at the path `presence/{userId}: {online: true, lastSeen: timestamp}`. Live quiz synchronization stores real-time quiz state at the path `quizzes/{quizId}/participants/{userId}: {currentQuestion, score, lastAnswerTime}`. Typing indicators for the doubt chat are stored at the path `doubts/{doubtId}/typing/{userId}: {isTyping: true}`.

### Firebase Cloud Messaging (FCM)

Notification types include session reminders, test results, quiz invitations, doubt responses, report availability, streak reminders ("Don't break your streak!"), leaderboard changes, and motivational quotes. Topic subscriptions include `subject_{subjectName}` for subject-specific quizzes, `school_{schoolName}` for school announcements, and `grade_{gradeNumber}` for grade-specific content.

### Firebase Crashlytics

This is enabled for crash reporting, non-fatal error logging, breadcrumb tracking for user flows (session started, test taken, quiz joined), and custom keys for debugging (user role, current screen, session state).

---

## 12. UI/UX Design System

### 12.1 — Color Palette

**Primary Theme — "Deep Focus Blue"**

The Primary color is #1A73E8 (a vibrant, trustworthy blue) used for primary buttons, active states, and the timer ring. The Primary Dark variant is #0D47A1 used for the status bar and app bar. The Primary Light variant is #BBDEFB used for backgrounds and chips.

The Secondary/Accent color is #FF6D00 (an energetic orange) used for streak indicators, XP badges, and reward highlights. The Secondary Dark variant is #E65100 and the Secondary Light variant is #FFE0B2.

The Success color is #00C853 used for passed tests, correct answers, and online status. The Error/Fail color is #FF1744 used for failed tests, broken streaks, and the crash animation. The Warning color is #FFD600 used for flags, pending states, and reminders. The Background for Light Theme is #F5F7FA and for Dark Theme is #121212. The Surface for Light Theme is #FFFFFF and for Dark Theme is #1E1E1E. Text on Light uses #1A1A2E as primary and #6B7280 as secondary. Text on Dark uses #FFFFFF as primary and #A0A0A0 as secondary.

### 12.2 — Typography

The heading font is Poppins (Google Fonts), a bold and modern typeface. H1 is 28sp Bold, H2 is 24sp SemiBold, H3 is 20sp SemiBold, and H4 is 18sp Medium. The body font is Inter (Google Fonts), a clean and highly legible typeface. Body 1 is 16sp Regular, Body 2 is 14sp Regular, and Caption is 12sp Regular. The timer display uses Space Mono (Google Fonts) in monospace at 48sp Bold. Button text uses Poppins at 16sp SemiBold, all caps.

### 12.3 — Iconography

The icon style uses Material Design 3 rounded icons as the base set, with custom icons for the Hill Climb vehicle, streak flame, XP star, and subject-specific icons. The icon sizes are 24dp for navigation and inline use, 32dp for cards and list items, and 48dp for feature highlights.

### 12.4 — Spacing & Layout

The base spacing unit is 8dp. The screen padding is 16dp horizontal. Card padding is 16dp all sides. The card corner radius is 16dp. Button corner radius is 12dp. The bottom navigation height is 64dp. The grid system uses a 4-column grid on phones and an 8-column grid on tablets.

### 12.5 — Elevation & Shadows

Cards use 2dp elevation with a subtle shadow. The bottom navigation uses 8dp elevation. Modal bottom sheets use 16dp elevation. The floating action button uses 6dp elevation. The lock screen overlay uses no shadow, favoring a blurred background instead.

### 12.6 — Animation Specifications

**Timer Ring:** A smooth, continuous clockwise fill using `ObjectAnimator` with `AccelerateDecelerateInterpolator`, running at 60fps.

**Confetti (Test Pass):** A particle system animation with 200 particles in primary and secondary colors, lasting 2 seconds with gravity effect, using Lottie or a custom Canvas implementation.

**Hill Climb Crash (Test Fail):** A Lottie animation of a cartoon vehicle flipping and tumbling down a hill for 3 seconds with screen shake (100ms, 5dp amplitude) and dust particle effects.

**Hill Climb Drive (Progress):** A Lottie animation of the vehicle driving uphill smoothly, triggered on XP gain with the distance proportional to XP earned.

**Screen Transitions:** Shared element transitions are used between screens. Cards expand to detail views. The bottom sheet slides up for settings and filters.

### 12.7 — Dark Mode

Dark mode is fully supported and matches the system setting by default. The dark palette uses #121212 as the background, #1E1E1E for surfaces, and the primary blue remains #1A73E8 (contrast-checked). All text, icons, and illustrations have dark mode variants. The timer screen defaults to dark mode regardless of the system setting (to reduce eye strain during study).

### 12.8 — Accessibility

The minimum touch target is 48dp × 48dp. Color contrast ratios meet WCAG AA standards (4.5:1 for text). TalkBack screen reader support is provided for all screens. Dynamic type scaling is supported up to 200%. Content descriptions are added for all images and icons.

---

## 13. Safety & Anti-Cheat Mechanisms (Expanded)

### 13.1 — Lock Screen Safety

**Emergency Unlock:** A password (set by the user, parent, or teacher) allows emergency unlock. Entering the emergency password breaks the current session, logs the event, and optionally notifies the parent. The emergency unlock is limited to 3 uses per day.

**Critical App Access:** The native Phone app is always accessible for emergencies (dialing 911 or local emergency numbers bypasses all locks). Whitelisted apps are always available.

**Device Safety:** If the battery drops below 5%, the lock is automatically suspended. If the device overheats (detected via BatteryManager), the session is paused.

### 13.2 — Anti-Bypass Measures

Device Administrator prevents uninstall while study mode is active. A Boot Receiver restarts the lock service on device reboot. An Accessibility Service detects force-stop attempts and restarts. Package Manager monitoring detects if the user tries to disable the app. ADB detection flags if USB debugging is connected during a session. Safe Mode detection alerts the user that bypassing resets the streak.

### 13.3 — Content Safety

All doubt-solving messages are filtered for inappropriate content using a keyword filter and AI moderation. Teacher verification is required before interacting with students. Call recording consent is mandatory (optional recording for moderation). Report and block functionality is available in all social features. All user-generated content (profile photos, doubt images) is scanned for inappropriate content.

---

## 14. Reward & Gamification Engine (Expanded)

### 14.1 — Hill Climb Racing Integration

The Hill Climb concept is not a separate game but a visual metaphor woven throughout the app.

**Vehicle Customization:** Users start with a basic car. As they level up, they unlock new vehicles including a bicycle at Level 1, a car at Level 2, a truck at Level 3, a monster truck at Level 4, a race car at Level 5, and a rocket at Level 6.

**Terrain Progression:** Each level has a different terrain background: a flat road for beginners, grassy hills for focused learners, rocky mountains for scholars, snowy peaks for warriors, volcanic terrain for champions, and outer space for legends.

**The Share Card:** Upon completing a significant achievement (level up, 30-day streak, leaderboard #1), a share card is generated showing the user's vehicle at the current terrain height, their stats (XP, streak, level), and a StudyLock watermark. The card is animated (2-second loop) for Instagram Stories.

### 14.2 — Voucher Fulfillment

Vouchers are digital, delivered as coupon codes in-app. Partner platforms include Google Play, Amazon, and education platforms (Coursera, Udemy). Voucher funding comes from a percentage of premium subscription revenue (see Monetization). Monthly voucher budget is capped per user to prevent abuse.

---

## 15. Notification Strategy

### Notification Types & Timing

**Morning Motivation** is sent at 7:00 AM with content like "🌅 Good morning! Today's quote: '[quote]'" and has high priority.

**Study Reminder** is sent at user-configured time (default 4:00 PM) with content like "📚 Time to study! Start a session now." and has high priority.

**Streak Warning** is sent at 9:00 PM if no session was completed that day, with content like "🔥 Don't lose your 12-day streak! Study for just 25 minutes." and has urgent priority.

**Quiz Available** is sent when a teacher publishes a quiz, with content like "🏆 New quiz in Mathematics! Compete now." and has high priority.

**Doubt Answered** is sent when a teacher responds, with content like "💡 Your doubt was answered by [Teacher Name]." and has high priority.

**Report Ready** is sent on Sunday or the first of the month, with content like "📊 Your weekly report is ready. See your progress!" and has normal priority.

**Leaderboard Change** is sent when the user's rank changes significantly, with content like "📈 You moved up to #5 on the leaderboard!" and has normal priority.

**Night Quiet** means no notifications are sent between 10:00 PM and 6:00 AM unless it is a streak warning.

---

## 16. Analytics & Reporting (Internal)

### Events to Track

The events tracked include app_opened, session_started (with properties: subject, timer_mode, duration), session_completed, session_abandoned (with property: minutes_completed), test_started, test_completed (with properties: score, passed, attempt_number), test_failed, quiz_joined (with property: quiz_type), quiz_completed (with properties: won, score, opponent_type), doubt_posted, doubt_resolved, call_started (with properties: type, context), reward_earned (with properties: reward_type, reward_name), share_card_generated, share_completed (with property: platform), leaderboard_viewed, profile_viewed, settings_changed (with property: setting_name), emergency_unlock_used, and anti_cheat_flag_triggered (with properties: flag_type, severity).

### Dashboards

The internal analytics dashboard tracks daily and monthly active users, average sessions per user per day, average study time per user per day, test pass rate, quiz participation rate, doubt resolution time, retention (D1, D7, D30), and churn rate.

---

## 17. Monetization Strategy

### Freemium Model

**Free Tier:** Users get up to 3 study sessions per day, access to the global leaderboard, basic quizzes (Quick Match), 5 doubt posts per day, and weekly reports.

**Premium Tier (\$4.99/month or \$39.99/year):** Premium offers unlimited study sessions, all leaderboards (school, subject, friends), all quiz types (classroom, challenge), unlimited doubt posts, monthly reports and PDF downloads, voice and video calls, AI-generated questions from user notes, advanced analytics, priority doubt routing to teachers, custom vehicle skins for the Hill Climb theme, and ad-free experience.

**Teacher Tier (\$9.99/month):** The teacher tier includes all premium features plus the ability to create unlimited classroom quizzes, class analytics dashboard, batch student management, priority support, and a custom question bank.

**Ads (Free Tier Only):** A banner ad appears on the Home Dashboard at the bottom. An interstitial ad plays after every third completed session. A rewarded video ad can be watched to earn 50 bonus XP (limited to 3 per day). No ads appear during study sessions, tests, or quizzes.

---

## 18. Release Phases & Roadmap

### Phase 1 — MVP (Months 1–3)

The deliverables include account creation (Firebase Auth) with student role only, the Pomodoro timer with classic and custom modes, home screen lock with whitelisted apps, the Test Gate with MCQ questions from a pre-loaded question bank, basic XP system and level progression, daily motivational quotes, the student home dashboard, basic progress graphs (study time and test scores), and settings page.

### Phase 2 — Social & Competition (Months 4–5)

The deliverables include the leaderboard (global and school), Quick Match online quizzes, the friend system, social share cards with the Hill Climb theme, a badge and sticker collection, the Hill Climb crash and drive animations, and push notifications (study reminders and streak warnings).

### Phase 3 — Teacher Ecosystem (Months 6–7)

The deliverables include teacher role and dashboard, classroom quiz creation and management, the doubt-solving system (text and image), teacher availability scheduling, class analytics for teachers, and student reports (weekly and monthly).

### Phase 4 — Advanced Features (Months 8–10)

The deliverables include voice and video calls (WebRTC), AI-generated questions from user notes (OCR plus AI), the advanced anti-cheat system, the voucher reward system, the premium subscription tier, dark mode, and tablet optimization.

### Phase 5 — Scale & Polish (Months 11–12)

The deliverables include performance optimization, localization (Hindi, Spanish, Portuguese), parent dashboard (view child's reports), a study groups feature (up to 4 users studying together with a shared timer), integration with Google Classroom, and an App Store Optimization campaign.

---

## 19. Risk Assessment

**Risk 1: Users find ways to bypass the lock screen.**
The mitigation strategy involves multiple layers of protection including Device Admin, Accessibility Service, and Boot Receiver. Regular security audits and updates will address newly discovered bypasses. The bypass detection system logs attempts and alerts the user that their streak will be affected.

**Risk 2: Low teacher adoption reduces the doubt-solving and quiz creation features' value.**
The mitigation strategy includes launching a teacher recruitment campaign through educational institutions. A bot teacher will handle basic doubt responses using AI when no human teacher is available. The community doubt-solving feature lets students help each other, reducing dependence on teachers.

**Risk 3: Users feel the lock screen is too restrictive and uninstall.**
The mitigation strategy includes always allowing access to essential apps (phone, messages, WhatsApp). The emergency unlock feature provides an escape hatch. Gradually increasing study session durations through onboarding prevents overwhelming new users. Clear communication of the value proposition emphasizes that the goal is to help, not punish.

**Risk 4: Question bank quality is insufficient.**
The mitigation strategy involves partnering with educational content providers. AI-generated questions supplement the manual bank. A teacher-contributed question bank grows organically. A question rating system deprioritizes poor questions.

**Risk 5: Scalability issues with real-time quiz matchmaking.**
The mitigation strategy uses Supabase Realtime with connection pooling. Bot opponents fill in when real opponents aren't available. Regional matchmaking reduces latency.

---

## 20. Success Metrics & KPIs

### User Acquisition

The Month 1 target is 10,000 downloads. The Month 6 target is 100,000 downloads. The Month 12 target is 500,000 downloads.

### Engagement

Daily Active Users (DAU) as a percentage of Monthly Active Users (MAU) should be at least 40%. The average number of study sessions per active user per day should be at least 2. The average daily study time per active user should be at least 45 minutes. The test pass rate target is 70% (indicating the questions are appropriately challenging). The quiz participation rate should reach at least 30% of DAU.

### Retention

D1 retention target is 60%. D7 retention target is 40%. D30 retention target is 25%.

### Monetization

The free-to-premium conversion rate target is 5%. The monthly recurring revenue target at Month 12 is \$50,000. The average revenue per user (ARPU) target is \$0.50.

### Quality

The app store rating target is 4.5 stars or above. The crash-free session rate target is 99.5%. The average doubt resolution time target is under 20 minutes. The anti-cheat flag rate target is below 2% (indicating most users are honest).

---

## Appendix A — Glossary

**Pomodoro Technique** is a time management method using timed intervals of focused work followed by short breaks. **Test Gate** is the quiz that appears after each study session that must be passed to unlock the phone. **Hill Position** is a numerical representation of the user's academic progress, visualized as a vehicle's position on a hill. **Integrity Score** is a 0–100 score assigned to each test based on the anti-cheat system's analysis. **Quick Match** is a real-time quiz between two students matched by subject and grade. **Edge Function** is a serverless function running on Supabase's edge network, used for matchmaking, report generation, and other backend logic.

---

## Appendix B — Competitive Analysis

**Forest App** focuses on gamified focus timing using virtual trees. It lacks testing, academic tracking, or social features. StudyLock differentiates by adding academic accountability through the Test Gate, competitive features through quizzes and leaderboards, and teacher interaction.

**Flipd** locks the phone for focus periods. It lacks academic testing, gamification depth, or teacher integration. StudyLock differentiates with the Hill Climb gamification, voucher rewards, and the complete teacher ecosystem.

**Quizlet** provides flashcards and study tools. It doesn't enforce study time or lock the phone. StudyLock differentiates by combining Quizlet's testing concept with enforced focus and real-time competition.

**Stay Focused** blocks apps with usage limits. It is purely a blocker with no educational features. StudyLock differentiates by converting blocked time into productive study time with measurable outcomes.

---

*End of Product Requirements Document — StudyLock v1.0*