USE [master]
GO
/****** Object:  Database [EducationSystem]    Script Date: 9/3/2024 9:40:45 AM ******/
CREATE DATABASE [EducationSystem]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'EducationSystem', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\EducationSystem.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'EducationSystem_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\EducationSystem_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [EducationSystem] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [EducationSystem].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [EducationSystem] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [EducationSystem] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [EducationSystem] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [EducationSystem] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [EducationSystem] SET ARITHABORT OFF 
GO
ALTER DATABASE [EducationSystem] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [EducationSystem] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [EducationSystem] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [EducationSystem] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [EducationSystem] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [EducationSystem] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [EducationSystem] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [EducationSystem] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [EducationSystem] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [EducationSystem] SET  ENABLE_BROKER 
GO
ALTER DATABASE [EducationSystem] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [EducationSystem] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [EducationSystem] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [EducationSystem] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [EducationSystem] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [EducationSystem] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [EducationSystem] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [EducationSystem] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [EducationSystem] SET  MULTI_USER 
GO
ALTER DATABASE [EducationSystem] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [EducationSystem] SET DB_CHAINING OFF 
GO
ALTER DATABASE [EducationSystem] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [EducationSystem] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [EducationSystem] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [EducationSystem] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [EducationSystem] SET QUERY_STORE = ON
GO
ALTER DATABASE [EducationSystem] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [EducationSystem]
GO
/****** Object:  Table [dbo].[Answer_Essay]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Answer_Essay](
	[answerId] [int] IDENTITY(1,1) NOT NULL,
	[questionId] [int] NULL,
	[user_essay] [text] NULL,
	[is_correct] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[answerId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Answer_M_Choice]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Answer_M_Choice](
	[answerId] [int] IDENTITY(1,1) NOT NULL,
	[questionId] [int] NULL,
	[user_choice] [text] NULL,
	[is_correct] [bit] NULL,
	[status] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[answerId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Comment]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Comment](
	[commentId] [int] IDENTITY(1,1) NOT NULL,
	[content] [text] NULL,
	[userId] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[commentId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Course]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Course](
	[courseId] [int] IDENTITY(1,1) NOT NULL,
	[teacherId] [int] NULL,
	[name] [varchar](255) NULL,
	[description] [text] NULL,
	[images] [varchar](255) NULL,
	[type] [varchar](50) NULL,
	[status] [bit] NULL,
	[fee] [decimal](10, 2) NULL,
	[quantity_session] [int] NULL,
	[start_datetime] [datetime] NULL,
	[end_datetime] [datetime] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[courseId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Course_Of_User]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Course_Of_User](
	[userId] [int] NOT NULL,
	[courseId] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[userId] ASC,
	[courseId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Docs_role]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Docs_role](
	[role_docsId] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[role_docsId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Documents]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Documents](
	[documentId] [int] IDENTITY(1,1) NOT NULL,
	[title] [varchar](255) NULL,
	[content] [text] NULL,
	[images] [varchar](255) NULL,
	[type] [varchar](50) NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
	[lessonId] [int] NULL,
	[doc_roleId] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[documentId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Exercises]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Exercises](
	[exercisesId] [int] IDENTITY(1,1) NOT NULL,
	[courseId] [int] NULL,
	[title] [varchar](255) NULL,
	[score] [decimal](5, 2) NULL,
	[status] [bit] NULL,
	[type] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[exercisesId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Grammar_Check]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Grammar_Check](
	[grammarId] [int] IDENTITY(1,1) NOT NULL,
	[questionId] [int] NULL,
	[userText] [text] NULL,
	[correct_text] [text] NULL,
	[error_json] [text] NULL,
	[score] [decimal](5, 2) NULL,
PRIMARY KEY CLUSTERED 
(
	[grammarId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Lesson]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Lesson](
	[lessonId] [int] IDENTITY(1,1) NOT NULL,
	[courseId] [int] NULL,
	[lesson_index] [int] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
	[name] [nvarchar](255) NOT NULL,
	[content] [text] NOT NULL,
	[status] [bit] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[lessonId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Question]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Question](
	[questionId] [int] IDENTITY(1,1) NOT NULL,
	[exerciseId] [int] NULL,
	[text] [text] NULL,
	[score] [decimal](5, 2) NULL,
	[question_type] [varchar](50) NULL,
	[skill_type] [varchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[questionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Result_exercises]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Result_exercises](
	[resultId] [int] IDENTITY(1,1) NOT NULL,
	[courseId] [int] NULL,
	[userId] [int] NULL,
	[status] [bit] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[resultId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Result_test]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Result_test](
	[resultId] [int] IDENTITY(1,1) NOT NULL,
	[userId] [int] NULL,
	[testId] [int] NULL,
	[status] [bit] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[resultId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Role]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Role](
	[roleId] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[roleId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Social]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Social](
	[socialId] [int] IDENTITY(1,1) NOT NULL,
	[userId] [int] NULL,
	[title] [varchar](255) NULL,
	[content] [text] NULL,
	[images] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[socialId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Test]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Test](
	[testId] [int] IDENTITY(1,1) NOT NULL,
	[title] [varchar](255) NULL,
	[courseId] [int] NULL,
	[level] [int] NULL,
	[start_datetime] [datetime] NULL,
	[end_datetime] [datetime] NULL,
	[date_done] [datetime] NULL,
	[score] [decimal](5, 2) NULL,
	[status] [bit] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[testId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Type_docs]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Type_docs](
	[typeId] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[typeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[userId] [int] IDENTITY(1,1) NOT NULL,
	[roleId] [int] NULL,
	[password] [varchar](255) NOT NULL,
	[username] [varchar](255) NOT NULL,
	[fullname] [varchar](255) NULL,
	[email] [varchar](255) NULL,
	[phone] [varchar](20) NULL,
	[description] [text] NULL,
	[level] [int] NULL,
	[status] [bit] NULL,
	[created_at] [datetime] NULL,
	[updated_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[userId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY],
UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Voice_Check]    Script Date: 9/3/2024 9:40:45 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Voice_Check](
	[voiceId] [int] IDENTITY(1,1) NOT NULL,
	[recorded_url] [varchar](255) NULL,
	[score] [decimal](5, 2) NULL,
	[questionId] [int] NULL,
	[error_json] [text] NULL,
PRIMARY KEY CLUSTERED 
(
	[voiceId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
ALTER TABLE [dbo].[Course] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Course] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Documents] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Documents] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Lesson] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Lesson] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Lesson] ADD  DEFAULT ((1)) FOR [status]
GO
ALTER TABLE [dbo].[Result_exercises] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Result_exercises] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Result_test] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Result_test] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Test] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[Test] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[User] ADD  DEFAULT (getdate()) FOR [updated_at]
GO
ALTER TABLE [dbo].[Answer_Essay]  WITH CHECK ADD FOREIGN KEY([questionId])
REFERENCES [dbo].[Question] ([questionId])
GO
ALTER TABLE [dbo].[Answer_M_Choice]  WITH CHECK ADD FOREIGN KEY([questionId])
REFERENCES [dbo].[Question] ([questionId])
GO
ALTER TABLE [dbo].[Comment]  WITH CHECK ADD FOREIGN KEY([userId])
REFERENCES [dbo].[User] ([userId])
GO
ALTER TABLE [dbo].[Course_Of_User]  WITH CHECK ADD FOREIGN KEY([courseId])
REFERENCES [dbo].[Course] ([courseId])
GO
ALTER TABLE [dbo].[Course_Of_User]  WITH CHECK ADD FOREIGN KEY([userId])
REFERENCES [dbo].[User] ([userId])
GO
ALTER TABLE [dbo].[Documents]  WITH CHECK ADD FOREIGN KEY([doc_roleId])
REFERENCES [dbo].[Docs_role] ([role_docsId])
GO
ALTER TABLE [dbo].[Documents]  WITH CHECK ADD FOREIGN KEY([lessonId])
REFERENCES [dbo].[Lesson] ([lessonId])
GO
ALTER TABLE [dbo].[Exercises]  WITH CHECK ADD FOREIGN KEY([courseId])
REFERENCES [dbo].[Course] ([courseId])
GO
ALTER TABLE [dbo].[Grammar_Check]  WITH CHECK ADD FOREIGN KEY([questionId])
REFERENCES [dbo].[Question] ([questionId])
GO
ALTER TABLE [dbo].[Lesson]  WITH CHECK ADD FOREIGN KEY([courseId])
REFERENCES [dbo].[Course] ([courseId])
GO
ALTER TABLE [dbo].[Question]  WITH CHECK ADD FOREIGN KEY([exerciseId])
REFERENCES [dbo].[Exercises] ([exercisesId])
GO
ALTER TABLE [dbo].[Result_exercises]  WITH CHECK ADD FOREIGN KEY([courseId])
REFERENCES [dbo].[Course] ([courseId])
GO
ALTER TABLE [dbo].[Result_exercises]  WITH CHECK ADD FOREIGN KEY([userId])
REFERENCES [dbo].[User] ([userId])
GO
ALTER TABLE [dbo].[Result_test]  WITH CHECK ADD FOREIGN KEY([testId])
REFERENCES [dbo].[Test] ([testId])
GO
ALTER TABLE [dbo].[Result_test]  WITH CHECK ADD FOREIGN KEY([userId])
REFERENCES [dbo].[User] ([userId])
GO
ALTER TABLE [dbo].[Social]  WITH CHECK ADD FOREIGN KEY([userId])
REFERENCES [dbo].[User] ([userId])
GO
ALTER TABLE [dbo].[Test]  WITH CHECK ADD FOREIGN KEY([courseId])
REFERENCES [dbo].[Course] ([courseId])
GO
ALTER TABLE [dbo].[User]  WITH CHECK ADD FOREIGN KEY([roleId])
REFERENCES [dbo].[Role] ([roleId])
GO
ALTER TABLE [dbo].[Voice_Check]  WITH CHECK ADD FOREIGN KEY([questionId])
REFERENCES [dbo].[Question] ([questionId])
GO
ALTER TABLE [dbo].[Course]  WITH CHECK ADD CHECK  (([type]='toeic' OR [type]='ilets'))
GO
ALTER TABLE [dbo].[Exercises]  WITH CHECK ADD CHECK  (([type]='essay' OR [type]='mutiple_choice'))
GO
ALTER TABLE [dbo].[Question]  WITH CHECK ADD CHECK  (([question_type]='essay' OR [question_type]='mutiple_choice'))
GO
ALTER TABLE [dbo].[Question]  WITH CHECK ADD CHECK  (([skill_type]='reading' OR [skill_type]='writting' OR [skill_type]='speaking' OR [skill_type]='listening'))
GO
USE [master]
GO
ALTER DATABASE [EducationSystem] SET  READ_WRITE 
GO
