import React, { useState } from 'react';
import './App.css';

function App() {
  const [userId, setUserId] = useState('1');
  const [ingredients, setIngredients] = useState('');
  const [threshold, setThreshold] = useState('0.7');
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('combined');

  const fetchRecommendations = async (endpoint) => {
    setLoading(true);
    setError(null);
    try {
      let url = `http://localhost:8080/${endpoint}?`;
      
      if (endpoint === 'strict-recommend') {
        const ingredientParams = ingredients.split(',')
          .map(ing => `userIngredients=${encodeURIComponent(ing.trim())}`)
          .join('&');
        url += `userId=${userId}&${ingredientParams}`;
      }
      else if (endpoint === 'recommend') {
        url += `userId=${userId}`;
      }
      else if (endpoint === 'filter') {
        url += `ingredients=${ingredients}&matchThreshold=${threshold}`;
      }
      else if (endpoint === 'recommend/filter') {
        url += `userId=${userId}&ingredients=${ingredients}&matchThreshold=${threshold}`;
      }

      const response = await fetch(url);
      if (!response.ok) throw new Error('Network response was not ok');
      const data = await response.json();
      if (data.error) {
        throw new Error(data.error);
      }
      setRecommendations(data);
    } catch (err) {
      setError(err.message || 'Failed to fetch recommendations');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (activeTab === 'basic') {
      fetchRecommendations('recommend');
    } else if (activeTab === 'filter') {
      fetchRecommendations('filter');
    } else if (activeTab === 'strict') {
      fetchRecommendations('strict-recommend');
    } else {
      fetchRecommendations('recommend/filter');
    }
  };

  return (
    <div className="app">
      <header className="header">
        <h1>Smart Recipe Recommender</h1>
      </header>

      <div className="tabs">
        <button 
          className={activeTab === 'combined' ? 'active' : ''}
          onClick={() => setActiveTab('combined')}
        >
          Combined Recommendations
        </button>
        <button 
          className={activeTab === 'basic' ? 'active' : ''}
          onClick={() => setActiveTab('basic')}
        >
          Basic Recommendations
        </button>
        <button 
          className={activeTab === 'filter' ? 'active' : ''}
          onClick={() => setActiveTab('filter')}
        >
          Filter by Ingredients
        </button>
        <button 
          className={activeTab === 'strict' ? 'active' : ''}
          onClick={() => setActiveTab('strict')}
        >
          Strict Recommendations
        </button>
      </div>

      <form onSubmit={handleSubmit} className="form">
        {activeTab === 'strict' ? (
          <>
            <div className="form-group">
              <label htmlFor="userId">User ID:</label>
              <input
                id="userId"
                type="number"
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                min="1"
                max="20"
                required
              />
            </div>
            <div className="form-group">
              <label htmlFor="ingredients">Ingredients (comma separated):</label>
              <input
                id="ingredients"
                type="text"
                value={ingredients}
                onChange={(e) => setIngredients(e.target.value)}
                placeholder="tomato, chicken, rice"
                required
              />
            </div>
            <div className="form-help">
              <p>Only shows recipes where you have <strong>all</strong> required ingredients</p>
            </div>
          </>
        ) : (
          <>
            {activeTab !== 'filter' && (
              <div className="form-group">
                <label htmlFor="userId">User ID:</label>
                <input
                  id="userId"
                  type="number"
                  value={userId}
                  onChange={(e) => setUserId(e.target.value)}
                  min="1"
                  max="20"
                  required
                />
              </div>
            )}
            {activeTab !== 'basic' && (
              <>
                <div className="form-group">
                  <label htmlFor="ingredients">Ingredients (comma separated):</label>
                  <input
                    id="ingredients"
                    type="text"
                    value={ingredients}
                    onChange={(e) => setIngredients(e.target.value)}
                    placeholder="tomato, chicken, rice"
                    required={activeTab !== 'basic'}
                  />
                </div>
                <div className="form-group">
                  <label htmlFor="threshold">Match Threshold (0-1):</label>
                  <input
                    id="threshold"
                    type="number"
                    value={threshold}
                    onChange={(e) => setThreshold(e.target.value)}
                    min="0"
                    max="1"
                    step="0.1"
                    required
                  />
                  <small>1.0 = strict, 0.1 = loose matching</small>
                </div>
              </>
            )}
          </>
        )}

        <button type="submit" disabled={loading}>
          {loading ? 'Loading...' : 'Get Recommendations'}
        </button>
      </form>

      {error && <div className="error">{error}</div>}

      <div className="results">
        <h2>Recommended Recipes</h2>
        {loading ? (
          <p>Loading recommendations...</p>
        ) : recommendations.length > 0 ? (
          <ul>
            {recommendations.map((recipe, index) => (
              <li key={index}>{recipe}</li>
            ))}
          </ul>
        ) : (
          !error && <p>No recipes found matching your criteria</p>
        )}
      </div>
    </div>
  );
}

export default App;
